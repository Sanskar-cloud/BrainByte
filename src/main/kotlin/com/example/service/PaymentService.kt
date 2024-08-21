package com.example.service

import com.example.data.models.Payment
import com.example.data.models.PaymentStatus
import com.example.data.models.StudentPayment
import com.example.data.repository.PaymentRepo
import com.example.data.repository.resource.ResourceRepository
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoDatabase

class PaymentService( private val paymentRepo: PaymentRepo){



    suspend fun findPaymentByOrderId(orderId: String): FindFlow<Payment> {

        return paymentRepo.findPaymentByOrderId(orderId)
    }
    suspend fun findPaymentByOrderIdStudent(orderId: String): FindFlow<StudentPayment> {

        return paymentRepo.findPaymentByOrderIdStudent(orderId)
    }

    suspend fun savePayment(payment: Payment) :Boolean{
        return paymentRepo.savePayment(payment)
    }
    suspend fun savePaymentStudent(payment2: StudentPayment) :Boolean{
        return paymentRepo.savePaymentStudent(payment2)
    }
    suspend fun updatePaymentStatus(paymentLinkId: String, status: PaymentStatus) {
        paymentRepo.updatePaymentStatus(paymentLinkId, status)
    }
    suspend fun updatePaymentStatusForTchr(paymentLinkId: String, status: PaymentStatus){
        paymentRepo.updatePaymentStatusForTchr(paymentLinkId, status)

    }

}