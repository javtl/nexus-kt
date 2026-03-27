package com.nexus

import com.nexus.com.nexus.plugins.configureRouting
import com.nexus.com.nexus.plugins.configureSerialization
import com.nexus.plugins.configureSecurity
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
}