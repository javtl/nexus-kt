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
    private val audience = environment.config.property("jwt.audience").getString()

    suspend fun fetchAgentToken(): String = withContext(Dispatchers.IO) {
        try {

            println("🔍 DEBUG: AuthService está a punto de disparar:")
            println("   - URL Destino: https://$domain/oauth/token")
            println("   - Usando ClientID: $clientId")
            println("   - Audience: https://$domain/api/v2/") // o la variable audience
            println("   - Secret Length: ${clientSecret.length}")

            val response: HttpResponse = httpClient.post("https://$domain/oauth/token") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "client_id" to clientId,
                        "client_secret" to clientSecret,
                        "audience" to "https://$domain/api/v2/",
                        "grant_type" to "client_credentials"
                    )
                )
            }
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                throw RuntimeException("Error Auth0: ${response.status}")
            }
        } catch(e: Exception){
                throw RuntimeException("Corroutine Auth Fail: ${e.message}")
            }
    }
}