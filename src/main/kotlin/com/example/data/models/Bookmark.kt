package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Bookmark(
    @SerialName("_id")
    val bookmarkId: String = ObjectId().toString(),
    val userId: String,
    val courseId: String,
    val course: CourseOverview?


)
