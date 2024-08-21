package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val categoryName: String,
    val categoryImageUrl: String? = null
)
