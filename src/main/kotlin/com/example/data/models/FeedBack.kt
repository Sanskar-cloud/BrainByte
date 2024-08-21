package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class FeedBack(
    @SerialName("_id")
    val feedbackId: String = ObjectId().toString(),
    val userId: String,
    val courseId: String,
    val feedback: String,
    val username: String,
    val profileImageUrl: String,
    val timestamp: Long,
)
