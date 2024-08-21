package com.example.data.repository.follow

import com.example.data.models.Following
import com.example.data.models.User
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList


class FollowRepositoryImpl(
    db: MongoDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>("Following")
    private val users = db.getCollection<User>("User")


    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String,
        app: Application
    ): Boolean {

        val doesFollowingUserExist = users.find(Filters.eq("_id",followingUserId)).firstOrNull()

        val doesFollowedUserExist = users.find(Filters.eq("_id",followedUserId)).firstOrNull()
        if(doesFollowingUserExist==null || doesFollowedUserExist==null) {
            return false
        }
        val up1 = users.updateOne(
            Filters.eq("_id",followingUserId),
            Updates.inc("followingCount",1)

        ).wasAcknowledged()
        app.log.info("updated user $up1")
        val up2 = users.updateOne(
            Filters.eq("_id",followedUserId),
            Updates.inc("followerCount",1)


        ).wasAcknowledged()
        app.log.info("updated user $up1")
        if (up1 && up2) {
            following.insertOne(
                Following(followedUserId = followedUserId, followingUserId = followingUserId)
            )
        }
        return true
    }

    override suspend fun unfollowUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val doesFollowingUserExist = users.find(Filters.eq("_id",followingUserId)).firstOrNull()
        val doesFollowedUserExist = users.find(Filters.eq("_id",followingUserId)).firstOrNull()
        if(doesFollowingUserExist==null || doesFollowedUserExist==null) {
            return false
        }
        val deleteResult = following.deleteOne(
           Filters.and(
               Filters.eq("followingUserId",followingUserId),
               Filters.eq("followedUserId",followedUserId)


            )
        )
        if(deleteResult.deletedCount > 0) {
            val ua1 = users.updateOne(
                Filters.eq("_id",followedUserId),
                Updates.inc("followingCount",-1)


            ).wasAcknowledged()
            val ua2 = users.updateOne(
                Filters.eq("_id",followedUserId),
                Updates.inc("followerCount",-1)

            ).wasAcknowledged()

            if (!ua1 or !ua2)
                return false
        }
        return deleteResult.deletedCount > 0
    }

    override suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean {
        val a= following.find(
            Filters.and(
                Filters.eq("followingUserId",followingUserId),
                Filters.eq("followedUserId",followedUserId)


            )
        ).firstOrNull()

        if(a==null){
            return false
        }
        else{
            return true
        }
    }

    override suspend fun getFollowsByUser(userId: String): List<Following> {
        return following.find(Filters.eq("followingUserId",userId)).toList()
    }
}