package com.example.data.repository

import com.example.data.models.Payment
import com.example.data.models.PaymentStatus
import com.example.data.models.StudentPayment
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class PaymentRepoImpl(db:MongoDatabase):PaymentRepo {
    private val payment=db.getCollection<Payment>("Payment")
    private val paymentStudent=db.getCollection<StudentPayment>("StudentPayment")
    override suspend fun findPaymentByOrderId(orderId: String): FindFlow<Payment> {
       return payment.find(Filters.eq("paymentLinkid",orderId))
    }

    override suspend fun findPaymentByOrderIdStudent(orderId: String): FindFlow<StudentPayment> {
        return paymentStudent.find(Filters.eq("paymentLinkid",orderId))
    }

    override suspend fun savePayment(payment2: Payment):Boolean {
        return payment.insertOne(payment2).wasAcknowledged()

    }

    override suspend fun savePaymentStudent(payment2: StudentPayment): Boolean {
        return paymentStudent.insertOne(payment2).wasAcknowledged()
    }

    override suspend fun updatePaymentStatus(paymentLinkId: String, status: PaymentStatus) {
        payment.updateOne(
            Filters.eq("paymentLinkid",paymentLinkId),
            Updates.combine(
                Updates.set("status",status)
            )




        )
    }

    override suspend fun updatePaymentStatusForTchr(paymentLinkId: String, status: PaymentStatus) {
        paymentStudent.updateOne(
            Filters.eq("paymentLinkid",paymentLinkId),
            Updates.combine(
                Updates.set("status",status)
            )




        )
    }
}