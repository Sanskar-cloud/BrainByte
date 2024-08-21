package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Project(
    @SerialName("_id")
    val projectId: String = ObjectId().toString(),
    val courseId: String,
    val projectName: String,
    val desc: String,
    val imageUrls: List<String>
)
