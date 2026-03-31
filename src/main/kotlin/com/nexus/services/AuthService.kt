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

    /**
     * Esta es la función que "abre el Vault".
     * Usamos fetchAgentToken porque Nexus Quant actúa como un Agente.
     */
    suspend fun fetchAgentToken(): String? = withContext(Dispatchers.IO) {
        return@withContext try {
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
                // Aquí deberías parsear el JSON para devolver solo el access_token string
                response.bodyAsText()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}