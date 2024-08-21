package com.example.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class PaymentLinkResponse(
    val paymentLinkUrl: String,
    val paymentLinkId: String
)