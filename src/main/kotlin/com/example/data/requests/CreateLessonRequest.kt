package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LessonRequest(
    val lessonNo: Int,
    val name: String,
    val desc: String,
)
