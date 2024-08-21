package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Lesson(
    @SerialName("_id")
    val lessonId: String = ObjectId().toString(),
    val courseId: String,
    val lessonNo: Int,
    val name: String,
    val thumbnail: String,
    val desc: String,
    val lessonVideoUrl: String
)