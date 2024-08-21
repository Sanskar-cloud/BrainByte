package com.example.data.repository.email

import com.example.data.requests.SendEmailRequestData
import com.example.data.responses.SendEmailResponse

interface MainRepo {
    suspend fun sendEmail(data: SendEmailRequestData): SendEmailResponse
}