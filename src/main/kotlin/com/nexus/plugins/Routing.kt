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
            try {
                val token = authService.fetchAgentToken()
                call.respondText("Token obtenido: $token")
            } catch (e: Exception) {
                call.respondText("Error detectado: ${e.localizedMessage}")
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
    }
}