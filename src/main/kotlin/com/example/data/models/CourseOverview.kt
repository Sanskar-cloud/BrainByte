package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CourseOverview(
    val courseId: String,
    val courseName: String,
    val courseTeacherName: String,
    val courseThumbnailUrl: String,
    val price:String?=null,
    val courseIntroVideoUrl: String?=null,
    val rating: Double? =0.0,
    val noOfStudentRated: Int? = null,
    val noOfStudentEnrolled: Int? = null,
    val tag: String? = null
)
