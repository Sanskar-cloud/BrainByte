package com.example

import com.example.data.models.User
import com.example.data.models.UserType
import com.example.data.repository.user.UserRepository
import com.example.data.repository.user.UserRepositoryImpl
import com.example.plugins.*
import com.example.util.Constants.DATABASE_NAME
import com.example.util.security.token.TokenConfig
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.http.*
import io.ktor.server.application.*

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.plugins.cors.CORS
import io.ktor.server.plugins.cors.routing.*


import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
@Suppress("unused")
@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {






    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    configureKoin()
    configureAuth(tokenConfig)
    configureHTTP()
    configureRouting()
    configureSerialization()
    configureMonitoring()





}
