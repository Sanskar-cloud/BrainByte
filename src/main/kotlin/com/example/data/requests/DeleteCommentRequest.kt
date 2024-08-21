package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCommentRequest(
    val commentId: String,
)