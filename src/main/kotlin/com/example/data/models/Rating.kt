package com.example.data.models

import com.example.util.DateSerializer
import kotlinx.serialization.Serializable

import java.util.*

@Serializable
data class Rating(
    val userId: String,
    val courseId: String,
    val ratingValue: Float,
    @Serializable(with = DateSerializer::class)
    val dateRating: Date
)

@Serializable
data class AvgRating(val avg_rating: Double)
