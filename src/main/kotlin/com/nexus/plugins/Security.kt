package com.nexus.plugins

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {
    val domain = environment.config.property("auth0.domain").getString()
    val clientId = environment.config.property("auth0.clientId").getString()
    val clientSecret = environment.config.property("auth0.clientSecret").getString()

    install(Authentication) {
        oauth("auth0") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "auth0",
                    authorizeUrl = "https://$domain/authorize",
                    accessTokenUrl = "https://$domain/oauth/token",
                    requestMethod = HttpMethod.Post,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    defaultScopes = listOf("openid", "profile", "email", "offline_access")
                )
            }
            client = HttpClient(Apache)
        }
    }
}