package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ResourceRequest(
    val courseId: String,
    val resourceUrl: List<String>
)
@Serializable
data class ResourceRequestForLesson(
//    val courseId: String,
    val lessonId:String,
    val resourceUrl: List<String>
)

