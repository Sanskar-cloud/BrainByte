package com.example.data.requests

import com.example.util.DurationSerializer
import kotlinx.serialization.Serializable

import kotlin.time.Duration

@Serializable
data class CourseRequest(
    val courseTitle: String,
    val description: String,
    val tag: String? = null,
    val price:String?=null,

    val duration: String
)

