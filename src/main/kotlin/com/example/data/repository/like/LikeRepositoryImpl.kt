package com.example.data.repository.like

import com.example.data.models.Comment
import com.example.data.models.Course
import com.example.data.models.Like
import com.example.data.models.User
import com.example.data.util.ParentType
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList



class LikeRepositoryImpl(
    db: MongoDatabase
): LikeRepository {

    private val likes = db.getCollection<Like>("Like")
    private val users = db.getCollection<User>("User")
    private val comments = db.getCollection<Comment>("Comment")
    private val courses = db.getCollection<Course>("Course")
    override suspend fun likeParent(userId: String, parentId: String, parentType: Int,app: Application): Boolean {
        val doesUserExist = users.find(Filters.eq("_id",userId)).firstOrNull()
        app.log.info("use exists or not $doesUserExist")
        return if(doesUserExist!=null) {
            when(parentType) {
                ParentType.Comment.type -> {
                    comments.updateOne(

                        Filters.eq("_id",parentId),
                        Updates.inc("likeCount",1)


                    )
                }
            }
            likes.insertOne(Like(userId, parentId, parentType, System.currentTimeMillis()))
            true
        } else {
            false
        }
    }

    override suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean {
        val doesUserExist = users.find(Filters.eq("_id",userId)).firstOrNull()
        return if(doesUserExist!=null) {
            when(parentType) {
                ParentType.Comment.type -> {
                    comments.updateOne(
                        Filters.eq("_id",parentId),
                        Updates.inc("likeCount",-1)
                    )
                }
            }
            likes.deleteOne(
                Filters.and(
                    Filters.eq("_id",userId),
                    Filters.eq("parentId",parentId)

                )
            )
            true
        } else {
            false
        }
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Filters.eq("parentId",parentId))
    }

    override suspend fun getLikesForParent(parentId: String, page: Int, pageSize: Int): List<Like> {
        return likes
            .find(Filters.eq("parentId",parentId))
            .skip(page * pageSize)
            .limit(pageSize)
            .sort(Sorts.descending("timestamp"))
            .toList()
    }
}