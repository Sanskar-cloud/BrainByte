package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.util.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun Application.configureAuth(config:TokenConfig) {

    install(Authentication) {

        jwt("auth-eduCo") {
            val jwtAudience = this@configureAuth.environment.config.property("jwt.audience").getString()
            val jwtSecret = System.getenv("JWT_SECRET")
            realm = this@configureAuth.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }

        }

    }
}

val JWTPrincipal.userId: String?
    get() = getClaim("userId", String::class)
