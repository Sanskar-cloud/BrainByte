package com.example.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val teacherId: String,
    val teacherName: String,
    val teacherEmail: String
)
@Serializable
data class PaymentRequestStudent(
    val StudentId: String,
    val StudentName: String,
    val StudentEmail: String,
    val courseId:String
)