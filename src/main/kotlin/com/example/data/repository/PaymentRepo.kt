package com.example.data.repository

import com.example.data.models.Payment
import com.example.data.models.PaymentStatus
import com.example.data.models.StudentPayment
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoDatabase

interface PaymentRepo{
    suspend fun findPaymentByOrderId(orderId: String): FindFlow<Payment>
    suspend fun findPaymentByOrderIdStudent(orderId: String): FindFlow<StudentPayment>
    suspend fun savePayment(payment: Payment):Boolean
    suspend fun savePaymentStudent(payment2: StudentPayment):Boolean
    suspend fun updatePaymentStatus(paymentLinkId: String, status: PaymentStatus)
    suspend fun updatePaymentStatusForTchr(paymentLinkId: String, status: PaymentStatus)
}