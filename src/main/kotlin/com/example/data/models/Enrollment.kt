package com.example.data.models

import com.example.util.DateSerializer
import kotlinx.serialization.Serializable

import java.util.Date

@Serializable
data class Enrollment(
    val userId: String,
    val courseId: String,
    @Serializable(with = DateSerializer::class)
    val dateEnrolled: Date
)
