package com.example.data.requests


import kotlinx.serialization.Serializable

@Serializable
data class RatingRequest(

    val ratingValue: Float
)
