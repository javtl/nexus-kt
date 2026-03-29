package com.nexus.services

import com.nexus.models.Auth0TokenResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*

class AuthService(
    private val httpClient: HttpClient,
    private val environment: ApplicationEnvironment
) {
    private val domain = environment.config.property("jwt.domain").getString()
    private val clientId = environment.config.property("auth0.clientId").getString()
    private val clientSecret = environment.config.property("auth0.clientSecret").getString()
    private val audience = environment.config.property("jwt.audience").getString()

    suspend fun fetchAgentToken(): String {
        val response: Auth0TokenResponse = httpClient.post("https://$domain/oauth/token") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "audience" to audience,
                "grant_type" to "client_credentials"
            ))
        }.body()

        return response.accessToken
    }
}