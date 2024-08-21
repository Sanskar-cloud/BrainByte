package com.example.service

import com.example.data.models.EmailData
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder

class DefaultEmailService(
    private val mailer: Mailer
) : EmailService {
    override suspend fun sendEmail(data: EmailData): Boolean {
        val userName = data.emailTo.split("@")[0]
        println(userName)
        val email = EmailBuilder.startingBlank()
            .from("Sanskar ", data.emailFrom)
            .to(userName, data.emailTo)
            .withSubject(data.subject)
            .withPlainText(data.message)
            .buildEmail()
        println(email)
        return try {
            println("djgvfpgihjgrger" +
                    "g'rger" +
                    "'gger'ggfggfg'grgf'ggfvbf'ggfgbvfdbvfbgfdgbvsbgfbvfvfbvvbfbvdfbvf")
            mailer.sendMail(email)
            println("djgvfpgihjgrger" +
                    "g'rger" +
                    "'gger'ggfggfg'grgf'ggfvbf'ggfgbvfdbvfbgfdgbvsbgfbvfvfbvvbfbvdfbvf")
            true
        }catch (e:Exception){
            e.printStackTrace()
            println(e)
            false
        }
    }
}