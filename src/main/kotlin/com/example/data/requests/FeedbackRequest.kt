package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val feedback: String,
    val timestamp: Long
)
