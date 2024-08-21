package com.example.service

import io.ktor.server.application.*
import com.example.data.models.Comment
import com.example.data.repository.comment.CommentRepository
import com.example.data.repository.user.UserRepository
import com.example.data.requests.CreateCommentRequest
import com.example.data.responses.CommentResponse
import com.example.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    suspend fun createComment(createCommentRequest: CreateCommentRequest, userId: String, app: Application): ValidationEvent {
        createCommentRequest.apply {
            if(comment.isBlank() || courseId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if(comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        val isCreatedComment = user.userType?.let {
            Comment(
                username = user.username,
                userType = it,
                profileImageUrl = user.profileImageUrl,
                likeCount = 0,
                comment = createCommentRequest.comment,
                userId = userId,
                courseId = createCommentRequest.courseId,
                timestamp = System.currentTimeMillis()

            )
        }?.let {
            commentRepository.createComment(
                it,
                app
            )
        }
        if (!isCreatedComment!!)
            return ValidationEvent.ServerError

        return ValidationEvent.Success
    }

    suspend fun deleteCommentsForCourse(postId: String) {
        commentRepository.deleteCommentsFromCourse(postId)
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>? {
        return commentRepository.getCommentsForCourse(courseId, ownUserId)
    }

    suspend fun getCommentById(commentId: String): Comment? {
        return commentRepository.getComment(commentId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object UserNotFound: ValidationEvent()
        object Success : ValidationEvent()

        object ServerError: ValidationEvent()
    }
}