package com.nexus.plugins

import com.nexus.models.GamerProfile
import com.nexus.repository.GamerRepository // <--- ASEGÚRATE DE ESTE IMPORT
import com.nexus.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// CAMBIO AQUÍ: de 'Unit' a 'GamerRepository'
fun Application.configureRouting(
    authService: AuthService,
    gamerRepository: GamerRepository
) {
    routing {
        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }

        get("/test-token") {
            get("/test-token") {
                try {
                    println("🔵 Iniciando petición asíncrona a Auth0...")
                    val token = authService.fetchAgentToken()
                    println("🟢 Token recibido con éxito")
                    call.respondText("Resultado: $token")
                } catch (e: Exception) {
                    println("🔴 Error en el test: ${e.message}")
                    call.respondText("Error en el flujo: ${e.localizedMessage}")
                }
            }
        }

        post("/auth/sync") {
            try {
                val profile = call.receive<GamerProfile>()
                //
                gamerRepository.saveOrUpdate(profile)
                call.respond(HttpStatusCode.Created, mapOf("status" to "Profile Synced"))
            } catch (e: Exception) {
                // Imprimimos el error en consola para saber qué falló (Mongo, JSON, etc)
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error saving profile: ${e.message}")
            }
        }

        get("/auth/agent-status") {
            // Ktor lanza esto en una corrutina automáticamente
            try {
                val tokenInfo = authService.fetchAgentToken()
                call.respondText("Conexión con Auth0 establecida: $tokenInfo")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Auth0 fuera de alcance")
            }
        }
    }
}