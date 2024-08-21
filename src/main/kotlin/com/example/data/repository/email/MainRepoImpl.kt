package com.example.data.repository.email

import com.example.data.models.EmailData
import com.example.data.requests.SendEmailRequestData
import com.example.data.responses.SendEmailResponse
import com.example.service.EmailService
import com.example.util.Constants

class MainRepoImpl(
    private val emailService: EmailService
):MainRepo{
     override suspend fun sendEmail(data: SendEmailRequestData): SendEmailResponse {
         val result = emailService.sendEmail(
             EmailData(
                 emailTo = data.email,
                 subject = data.subject,
                 message = data.message,
                 emailFrom = Constants.EMAIL_FROM
             )
         )
         println(result)
         return SendEmailResponse(
             success = result,
             status = if (result) 200 else 400,
             message = if (result) "Successfully sent email" else "Failed to send email"
         )
     }

}

