package com.nexus.plugins


import com.nexus.models.GamerProfile
import com.nexus.repository.gamerRepository
import com.nexus.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting(authService: AuthService, gamerRepository: gamerRepository) {
    routing {
        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }

        get("/test-token") {
            try {
                val token = authService.fetchAgentToken()
                call.respondText("Token obtenido: $token")
            } catch (e: Exception) {
                // Esto nos dirá el error real en la pantalla del navegador
                call.respondText("Error detectado: ${e.localizedMessage}")
            }
        }

        post("/auth/sync") {
            // 1. En el futuro, aquí validaremos el JWT del usuario
            // Por ahora, simulamos que recibimos los datos del perfil
            val profile = call.receive<GamerProfile>()

            try {
                gamerRepository.saveOrUpdate(profile)
                call.respond(HttpStatusCode.Created, mapOf("status" to "Profile Synced"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error saving profile")
            }
        }
    }
}