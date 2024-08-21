package com.example.service

import com.example.data.models.EmailData

interface EmailService {
    suspend fun sendEmail(data: EmailData):Boolean
}