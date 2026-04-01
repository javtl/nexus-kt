package com.nexus.plugins

import com.nexus.models.GamerProfile
import com.nexus.repository.GamerProfileRepository
import com.nexus.routes.actionRouting
import com.nexus.routes.dashboardRouting
import com.nexus.services.ActionService
import com.nexus.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authService: AuthService,
    gamerRepository: GamerProfileRepository,
    actionService: ActionService
) {
    routing {

        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }

        // 1. Test de Token
        get("/test-token") {
            try {
                val token = authService.fetchAgentToken()
                call.respondText("Resultado: $token")
            } catch (e: Exception) {
                call.respondText("Error: ${e.localizedMessage}")
            }
        }

        // 2. Sincronización de Perfiles (Postman envía aquí)
        post("/auth/sync") {
            try {
                val profile = call.receive<GamerProfile>()
                gamerRepository.saveProfile(profile)
                call.respond(HttpStatusCode.Created, mapOf("status" to "Profile Synced"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }

        // 3. Status de Agente
        get("/auth/agent-status") {
            try {
                val tokenInfo = authService.fetchAgentToken()
                call.respondText("Conexión Auth0 OK: $tokenInfo")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Auth0 Offline")
            }
        }

        // --- LAS RUTAS MODULARES ---

        // Esto inyecta las rutas de acciones
        actionRouting(actionService)

        // Esto inyecta la ruta /dashboard (usa el archivo DashboardRoutes.kt)
        dashboardRouting(gamerRepository)
    }
}