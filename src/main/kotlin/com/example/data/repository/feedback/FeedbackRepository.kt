package com.example.data.repository.feedback
import com.example.data.models.FeedBack
import com.example.data.requests.FeedbackRequest


interface FeedbackRepository {
    suspend fun getFeedBack(feedbackId: String): FeedBack?
    suspend fun getAllFeedBacks(courseId: String): List<FeedBack>
    suspend fun createFeedback(userId: String, courseId: String, request: FeedbackRequest): Boolean
    suspend fun updateFeedback(feedbackId: String, feedback: FeedbackRequest): Boolean
    suspend fun deleteFeedback(feedbackId: String): Boolean
    suspend fun deleteAllFeedback(courseId: String): Boolean
}