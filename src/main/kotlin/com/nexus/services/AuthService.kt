package com.nexus.services

import io.ktor.client.*
import io.ktor.client.call.* // 👈 IMPORTANTE: Para que funcione .body()
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.serialization.Serializable

@Serializable
data class Auth0TokenResponse(
    val access_token: String,
    val scope: String? = null,
    val expires_in: Int,
    val token_type: String
)

class AuthService(
    private val httpClient: HttpClient,
    private val environment: ApplicationEnvironment
) {
    private val domain = environment.config.property("auth0.domain").getString()
    private val clientId = environment.config.property("auth0.clientId").getString()
    private val clientSecret = environment.config.property("auth0.clientSecret").getString()
    private val audience = environment.config.property("auth0.audience").getString() // 👈 FALTABA ESTO

    suspend fun fetchAgentToken(): String {
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

        return if (response.status == HttpStatusCode.OK) {
            val data = response.body<Auth0TokenResponse>() // 👈 Uso correcto de .body()
            data.access_token // 👈 Usamos el nombre exacto del JSON
        } else {
            throw RuntimeException("Auth0 API Error: ${response.status}")
        }
    }
}