package com.nexus.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.*
import io.ktor.server.request.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        // Nivel de log: INFO es el estándar para desarrollo
        level = Level.INFO

        // Filtro: Solo logueamos las rutas que nos interesan (evita ruido)
        filter { call -> call.request.path().startsWith("/") }

        // Formato: Esto hará que veas algo como "GET /users/123 - 200 OK"
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "HTTP Method: $httpMethod, Status: $status, User agent: $userAgent"
        }
    }
}