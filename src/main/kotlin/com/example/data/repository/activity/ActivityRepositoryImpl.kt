package com.example.data.repository.activity

import com.example.data.models.Activity
import com.example.data.models.User
import com.example.data.responses.ActivityResponse
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts


import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class ActivityRepositoryImpl(
    db: MongoDatabase
): ActivityRepository {

    private val users = db.getCollection<User>("User")
    private val activities = db.getCollection<Activity>("Activty")

    override suspend fun getActivitiesForUser(userId: String, page: Int, pageSize: Int): List<ActivityResponse> {
        val activities = activities
            .find(Filters.eq("toUserId", userId))
            .skip(page * pageSize)
            .limit(pageSize)
            .sort(Sorts.descending("timestamp"))
            .toList()
        val userIds = activities.map { it.byUserId }
        val users = users.find(Filters.`in`("id",userIds)).toList()
        return activities.mapIndexed { i, activity ->
            ActivityResponse(
                timestamp = activity.timestamp,
                userId = activity.byUserId,
                parentId = activity.parentId,
                type = activity.type,
                username = users[i].username,
                id = activity.id
            )
        }
    }

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {

        val result = activities.deleteOne(Filters.eq("_id", ObjectId(activityId)))
        return result.wasAcknowledged()
    }
}