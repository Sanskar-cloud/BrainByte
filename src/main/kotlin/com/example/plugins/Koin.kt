package com.example.plugins

import io.ktor.server.application.*
import com.example.di.getKoinModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin){
        slf4jLogger()
        modules(getKoinModule())
    }
}