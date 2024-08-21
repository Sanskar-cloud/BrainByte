package com.example.data.repository.feedback

import com.example.data.models.FeedBack
import com.example.data.models.User
import com.example.data.requests.FeedbackRequest
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList



class FeedbackRepositoryImpl(
    db: MongoDatabase
): FeedbackRepository {

    private val feedbacks = db.getCollection<FeedBack>("FeedBack")
    private val users = db.getCollection<User>("User")

    override suspend fun getFeedBack(feedbackId: String): FeedBack? {
        return feedbacks.find(Filters.eq("feedbackId",feedbackId)).firstOrNull()
    }

    override suspend fun getAllFeedBacks(courseId: String): List<FeedBack> {
        return feedbacks.find(Filters.eq("courseId",courseId)).toList()
    }

    override suspend fun createFeedback(userId: String, courseId: String, request: FeedbackRequest): Boolean {
        val user = users.find(Filters.eq("courseId",courseId)).firstOrNull()
        if(user!=null){
            return feedbacks.insertOne(
                FeedBack(
                    userId = userId,
                    courseId = courseId,
                    feedback = request.feedback,
                    username = user.username,
                    profileImageUrl = user.profileImageUrl,
                    timestamp = request.timestamp
                )
            ).wasAcknowledged()

        }
        else{
            return false
        }

    }

    override suspend fun updateFeedback(feedbackId: String, feedback: FeedbackRequest): Boolean {
        return feedbacks.updateOne(
            Filters.eq("feedbackId", feedbackId),
            Updates.combine(
                Updates.set("feedback", feedback.feedback),
                Updates.set("timestamp", feedback.timestamp)
            )


        ).wasAcknowledged()

    }

    override suspend fun deleteFeedback(feedbackId: String): Boolean {
        return feedbacks.deleteOne(Filters.eq("feedbackId", feedbackId)).wasAcknowledged()
    }

    override suspend fun deleteAllFeedback(courseId: String): Boolean {
        return feedbacks.deleteMany(Filters.eq("courseId", courseId)).wasAcknowledged()
    }

}