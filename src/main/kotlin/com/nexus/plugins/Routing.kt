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
    gamerRepository: GamerProfileRepository, // <-- Este es el nombre oficial del parámetro
    actionService: ActionService
) {
    routing {

        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }

        get("/test-token") {
            try {
                val token = authService.fetchAgentToken()
                call.respondText("Resultado: $token")
            } catch (e: Exception) {
                call.respondText("Error en el flujo: ${e.localizedMessage}")
            }
        }

        // 3. Sincronización de Perfiles (Gamer Data)
        post("/auth/sync") {
            try {
                val profile = call.receive<GamerProfile>()
                // SOLUCIÓN: Usamos 'saveProfile' que es el método que dejamos en el repo
                gamerRepository.saveProfile(profile)
                call.respond(HttpStatusCode.Created, mapOf("status" to "Profile Synced"))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error saving profile: ${e.message}")
            }
        }

        get("/auth/agent-status") {
            try {
                val tokenInfo = authService.fetchAgentToken()
                call.respondText("Conexión con Auth0 establecida: $tokenInfo")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Auth0 fuera de alcance")
            }
        }

        // 5. REGISTRO DEL ACTION ENGINE
        actionRouting(actionService)

        // 6. DASHBOARD (PASANDO EL REPO CORRECTO)
        // SOLUCIÓN: Le pasamos 'gamerRepository' porque así se llama arriba en la función
        dashboardRouting(gamerRepository)
    }
}