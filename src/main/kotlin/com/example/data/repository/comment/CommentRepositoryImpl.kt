package com.example.data.repository.comment

import io.ktor.server.application.*
import com.example.data.models.Comment
import com.example.data.models.Course
import com.example.data.models.Like
import com.example.data.responses.CommentResponse
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.inc
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId


class CommentRepositoryImpl(
    db: MongoDatabase
): CommentRepository {

    private val courses = db.getCollection<Course>("Course")
    private val comments = db.getCollection<Comment>("Comment")
    private val likes = db.getCollection<Like>("Like")

    override suspend fun createComment(comment: Comment, app: Application): Boolean {
        comments.insertOne(comment)
        return courses.updateOne(
            Filters.eq("_id", (comment.courseId)),


           Updates.inc("commentCount", 1)
        ).wasAcknowledged()
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteResult = comments.deleteOne(Filters.eq("id", ObjectId(commentId)))
        return deleteResult.wasAcknowledged() && deleteResult.deletedCount > 0

    }

    override suspend fun deleteCommentsFromCourse(courseId: String): Boolean {
        val deleteResult = comments.deleteMany(Filters.eq("courseId", courseId))
        return deleteResult.wasAcknowledged()
    }
    override suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>? {
        val commentsList = comments.find(Filters.eq("courseId", courseId)).toList()
        return commentsList.map { comment ->
            val isLiked = likes.find(
                Filters.and(
                    Filters.eq("userId", ownUserId),
                    Filters.eq("parentId", comment.id)
                )
            ).firstOrNull() != null
            CommentResponse(
                id = comment.id,
                username = comment.username,
                profilePictureUrl = comment.profileImageUrl,
                timestamp = comment.timestamp,
                comment = comment.comment,
                isLiked = isLiked,
                likeCount = comment.likeCount,
                userType = comment.userType
            )
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.find(Filters.eq("id", ObjectId(commentId))).firstOrNull()
    }

}