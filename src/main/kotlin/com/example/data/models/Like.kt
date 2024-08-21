package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Like(
    val userId: String,
    val parentId: String,
    val parentType: Int,
    val timestamp: Long,
    @SerialName("_id")
    val id: String = ObjectId().toString(),
)