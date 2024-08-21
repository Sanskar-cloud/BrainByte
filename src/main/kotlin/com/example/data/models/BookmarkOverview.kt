package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class BookmarkOverview(
    @SerialName("_id")
    val bookmarkId: String = ObjectId().toString(),
    val userId: String,
    val courseId: String,
    val course:List<CourseOverview>

)