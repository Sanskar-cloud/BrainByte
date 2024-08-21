package com.example.data.models

import UserTchr
import com.example.util.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

import kotlin.time.Duration


@Serializable
data class Course(
    @SerialName("_id")
    val courseId: String = ObjectId().toString(),
    val userId: String = "",
    val courseTitle: String,
    val courseThumbnail: String,
    val courseIntroVideoUrl: String,
    val price:String?=null,
    val description: String,
    val moreDetails: String? = null,
    val courseTeacher : UserTchr? = null,
    val noOfStudentEnrolled: Int = 0,
    val noOfLessons: Int,
    val noOfStudentRated: Int = 0,
    val avgRating: Double = 0.0,
    val tag: String? = null,
    val commentCount: Int = 0,

    val duration: String? = null,

    )