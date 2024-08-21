package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val instagramUrl: String,
    val facebookUrl: String,

    val twitterUrl: String,
)
