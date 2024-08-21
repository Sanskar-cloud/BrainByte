package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Comment(
    @SerialName("_id")
    val id: String = ObjectId().toString(),
    val userId: String,
    val userType:String,
    val courseId: String,
    val username: String,
    val profileImageUrl: String,
    val comment: String,
    val likeCount: Int,
    val timestamp: Long,
)