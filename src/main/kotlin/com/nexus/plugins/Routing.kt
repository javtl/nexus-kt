package com.nexus.com.nexus.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Nexus.kt: Bridge Status ONLINE 🟢")
        }
    }
}