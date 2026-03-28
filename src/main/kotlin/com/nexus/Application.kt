package com.nexus

import com.nexus.com.nexus.plugins.configureRouting
import com.nexus.com.nexus.plugins.configureSerialization
import com.nexus.database.DatabaseFactory
import com.nexus.plugins.configureMonitoring
import com.nexus.plugins.configureSecurity
import io.ktor.server.application.*

// El punto de entrada del motor Netty
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {


    DatabaseFactory.init(environment)

    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}