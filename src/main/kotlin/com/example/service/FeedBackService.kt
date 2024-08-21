package com.example.service

import com.example.data.models.FeedBack
import com.example.data.repository.feedback.FeedbackRepository
import com.example.data.requests.FeedbackRequest

class FeedBackService(
    private val feedbackRepository: FeedbackRepository
) {

    suspend fun getFeedBack(feedbackId: String): FeedBack? {
        return feedbackRepository.getFeedBack(feedbackId)
    }
    suspend fun getAllFeedBacks(courseId: String): List<FeedBack> {
        return feedbackRepository.getAllFeedBacks(courseId)
    }
    suspend fun createFeedback(userId: String, courseId: String, request: FeedbackRequest): Boolean {
        return feedbackRepository.createFeedback(
            userId,
            courseId,
            request
        )
    }
    suspend fun updateFeedback(feedbackId: String, feedback: FeedbackRequest): Boolean {
        return feedbackRepository.updateFeedback(feedbackId, feedback)
    }
    suspend fun deleteFeedback(feedbackId: String): Boolean {
        return feedbackRepository.deleteFeedback(feedbackId)
    }
    suspend fun deleteAllFeedback(courseId: String): Boolean {
        return feedbackRepository.deleteAllFeedback(courseId)
    }

}