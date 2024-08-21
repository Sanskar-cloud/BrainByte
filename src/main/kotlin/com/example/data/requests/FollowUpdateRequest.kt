package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FollowUpdateRequest(
    val followedUserId: String
)
