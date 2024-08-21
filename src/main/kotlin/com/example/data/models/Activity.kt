package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Activity(
    val timestamp: Long,
    val byUserId: String,
    val toUserId: String,
    val type: Int,
    val parentId: String,
    @SerialName("_id")
    val id: String = ObjectId().toString(),
)
