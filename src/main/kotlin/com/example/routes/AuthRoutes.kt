package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.repository.email.MainRepo
import com.example.data.requests.*
import com.example.data.responses.AuthResponse
import com.example.data.responses.BasicApiResponse
import com.example.data.responses.SendEmailResponse
import com.example.service.UserService
import com.example.util.ApiResponseMessages.FIELDS_BLANK
import com.example.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.example.util.ApiResponseMessages.USER_ALREADY_EXISTS
import com.example.util.security.hashing.HashingService
import com.example.util.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.PasswordAuthentication
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

//import org.apache.commons.codec.digest.DigestUtils
import java.util.*

val otpMap = mutableMapOf<String, String>()


fun Route.createUser(userService: UserService, hashingService: HashingService) {
    post("/api/user/create") {
        val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userService.doesUserWithEmailExist(request.email)) {
            call.respond(
                BasicApiResponse<Unit>(
                    successful = false,
                    message = USER_ALREADY_EXISTS
                )
            )
            return@post
        }

        when (userService.validateCreateAccountRequest(request)) {
            is UserService.ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
            }
            is UserService.ValidationEvent.Success -> {
                if (request.accountType == "Teacher" && request.accountDetails == null) {
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = "Account details are required for teachers"
                        )
                    )
                    return@post
                }

                userService.createUser(request, hashingService)
                call.respond(
                    BasicApiResponse<Unit>(successful = true)
                )
            }

            else -> {}
        }
    }
}

fun Route.loginUser(
    hashingService: HashingService,
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest,"HII")
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (isValidPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = user.userType?.let { it1 ->
                        AuthResponse(
                            userId = user.id,
                            token = token,
                            userType = it1

                        )
                    }
                )
            )
        } else {
//            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }
    }
}

fun Route.resetPassword(userService: UserService, hashingService: HashingService) {
    authenticate("auth-eduCo") {
        post("/api/user/reset_password") {
            val request = call.receiveOrNull<ResetPasswordRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = userService.getUserById(call.userId)

            val isCorrectOldPassword = hashingService.verify(
                value = request.oldPassword,
                saltedHash = SaltedHash(
                    hash = user!!.password,
                    salt = user.salt
                )
            )

            if (!isCorrectOldPassword) {
                call.respond(
                    BasicApiResponse<Unit>(successful = false)
                )
                return@post
            }

            val saltedHash = hashingService.generateSaltedHash(request.newPassword)
            userService.updatePasswordForUser(call.userId, hash = saltedHash.hash, salt = saltedHash.salt)
            call.respond(
                BasicApiResponse<Unit>(successful = true)
            )
        }
    }
}

fun Route.authenticate() {
    authenticate("auth-eduCo") {
        get("/api/user/authenticate") {
            call.respond(HttpStatusCode.OK, message = "you are successfully authenticated")
        }
    }
}

fun Route.sendEmailRoutes(mainRepo: MainRepo){

    post("/send-email") {
        val requestData = call.receiveNullable<SendEmailRequestData>()
            ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                SendEmailResponse(
                    status = 400,
                    success = false,
                    message = "Missing request params"
                )
            )
        val responseData = mainRepo.sendEmail(requestData)
        call.respond(
            if (responseData.success) HttpStatusCode.OK else HttpStatusCode.BadRequest,
            responseData
        )
    }

}
fun Route.generateOtp(){
    post("/api/generateOtp") {
        val request = call.receiveOrNull<GenerateOtpRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val email = request.email// Assuming you receive the email address in the request body
        val otp = generateOTP()
        println(otp)


        otpMap[email]=otp
        println(otpMap)

        try {
            sendOtpEmail(email, otp)
            call.respond(HttpStatusCode.OK, "OTP sent successfully")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to send OTP")
        }
    }



}
fun generateOTP(): String {
    // Generate a random 6-digit OTP
    return (100000..999999).random().toString()
}

suspend fun sendOtpEmail(email: String, otp: String) {
    withContext(Dispatchers.IO) {
        // Mail server configuration
        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            println("ghus gy")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.example.com") // Replace with your SMTP host
            put("mail.smtp.port", "587") // Replace with your SMTP port
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                println("ghusgys")
                return PasswordAuthentication("sanskarbhadani11@gmail.com", "casj ryuz hyea fdqg") // Replace with your email credentials
            }
        })

        try {
            val message = MimeMessage(session).apply {
                println(session)
                setFrom(InternetAddress("sanskarbhadani11@gmail.com")) // Replace with your email address
                addRecipient(Message.RecipientType.TO, InternetAddress(email))
                subject = "Your OTP for Signup"
                setText("Your OTP is: $otp")
                println(otp)
            }

            Transport.send(message)
            println("tran")
        } catch (e: MessagingException) {
            throw RuntimeException("Failed to send OTP email", e)
        }
    }
}
fun verifyOtp(email: String, enteredPassword: String): Boolean {

    val storedOtp = retrieveStoredOtp(email)


    return enteredPassword == storedOtp
}

// Example function to retrieve stored OTP from a database or cache
fun retrieveStoredOtp(email: String): String {

    return otpMap[email] ?: ""
}

