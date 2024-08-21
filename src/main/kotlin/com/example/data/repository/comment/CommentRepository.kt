package com.example.data.repository.comment

import io.ktor.server.application.*
import com.example.data.models.Comment
import com.example.data.responses.CommentResponse

interface CommentRepository {

    suspend fun createComment(comment: Comment, app: Application): Boolean

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun deleteCommentsFromCourse(courseId: String): Boolean

    suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>?

    suspend fun getComment(commentId: String): Comment?
}