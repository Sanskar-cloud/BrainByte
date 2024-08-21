package com.example.data.responses

import com.example.data.models.UserType
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: String,
    val username: String,
    val userType: String,
    val profilePictureUrl: String,
    val timestamp: Long,
    val comment: String,
    val isLiked: Boolean,
    val likeCount: Int
)
