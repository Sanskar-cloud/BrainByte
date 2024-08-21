package com.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.Instant
@Serializable
data class Payment(
    @SerialName("_id")
    var id: String = ObjectId().toString(),
    val teacherId: String,
    val amount: Int,
    val currency: String,

    var paymentLinkid:String,
    var status: PaymentStatus = PaymentStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()

)
