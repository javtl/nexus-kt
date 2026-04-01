package com.nexus.plugins

import com.nexus.models.GamerProfile
import com.nexus.repository.GamerRepository
import com.nexus.routes.actionRouting // <--- Importante: Importa la extensión de rutas de acción
import com.nexus.routes.dashboardRouting
import com.nexus.services.ActionService
import com.nexus.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * ConfigureRouting: El orquestador de puntos de entrada.
 * Aquí conectamos los servicios (cerebro) con los endpoints (carrocería).
 */
fun Application.configureRouting(
    authService: AuthService,
    gamerRepository: GamerRepository,
    actionService: ActionService // <--- NUEVO: Inyectamos el motor de acciones
) {
    routing {

        // 1. Endpoints Base
        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }

        // 2. Endpoints de Diagnóstico (M2M Auth0)
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

        // 3. Sincronización de Perfiles (Gamer Data)
        post("/auth/sync") {
            try {
                val profile = call.receive<GamerProfile>()
                gamerRepository.saveOrUpdate(profile)
                call.respond(HttpStatusCode.Created, mapOf("status" to "Profile Synced"))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error saving profile: ${e.message}")
            }
        }

        // 4. Status del Agente Soberano
        get("/auth/agent-status") {
            try {
                val tokenInfo = authService.fetchAgentToken()
                call.respondText("Conexión con Auth0 establecida: $tokenInfo")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Auth0 fuera de alcance")
            }
        }

        // 5. REGISTRO DEL ACTION ENGINE (ID: H)
        // Aquí es donde Nexus Quant empieza a escuchar órdenes de la IA
        actionRouting(actionService)

        dashboardRouting()
    }
}