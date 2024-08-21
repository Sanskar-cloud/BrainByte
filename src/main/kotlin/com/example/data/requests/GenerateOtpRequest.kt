package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class GenerateOtpRequest (
    val email:String
)
