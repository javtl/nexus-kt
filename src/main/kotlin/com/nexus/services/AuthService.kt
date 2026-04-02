package com.nexus.services

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthService(
    private val httpClient: HttpClient,
    private val environment: ApplicationEnvironment
) {
    private val domain = environment.config.property("auth0.domain").getString()
    private val clientId = environment.config.property("auth0.clientId").getString()
    private val clientSecret = environment.config.property("auth0.clientSecret").getString()

    suspend fun fetchAgentToken(): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val response = httpClient.post("https://$domain/oauth/token") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "client_id" to clientId,
                    "client_secret" to clientSecret,
                    "audience" to audience,
                    "grant_type" to "client_credentials"
                )
            )
        }

        if (response.status == HttpStatusCode.OK) {
            val data: Auth0TokenResponse = response.body()
            data.accessToken
        } else {
            val errorMsg = response.status.value.toString()
            throw RuntimeException("Auth0 API Error: $errorMsg")
        }
    }
}