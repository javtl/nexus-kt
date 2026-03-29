package com.nexus.plugins


import com.nexus.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting(authService: AuthService) {
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
    }
}