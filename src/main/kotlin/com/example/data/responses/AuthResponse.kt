package com.example.data.responses

import com.example.data.models.UserType
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: String,
    val token: String,
    val userType: String
)