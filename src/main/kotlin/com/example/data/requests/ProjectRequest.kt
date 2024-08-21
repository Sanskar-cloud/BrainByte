package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ProjectRequest(
    val projectName: String,
    val desc: String,
    val imageUrls: List<String>
)
