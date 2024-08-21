package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int,
    val userid:String
)
