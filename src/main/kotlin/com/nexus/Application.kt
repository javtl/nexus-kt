package com.nexus

import com.nexus.com.nexus.plugins.configureSerialization
import com.nexus.plugins.* // Para traer configureHttpClient y otros
import com.nexus.database.DatabaseFactory
import com.nexus.repository.gamerRepository
import com.nexus.services.AuthService // Asegúrate de que este import sea correcto
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    // 1. Inicializamos la DB
    DatabaseFactory.init(environment)

    // 2. CREAMOS EL CLIENTE HTTP (Necesario para el Vault)
    val httpClient = configureHttpClient()

    // 3. INSTANCIAMOS EL SERVICIO DE AUTH (El Vault)
    // Le pasamos el cliente y el 'environment' para que lea el application.conf
    val authService = AuthService(httpClient, environment)

    // 4. CONFIGURAMOS LOS PLUGINS (Pasando el authService donde toca)
    configureSecurity()
    configureSerialization()
    configureMonitoring()

    // Ahora 'authService' ya existe y se lo podemos pasar a las rutas
    configureRouting(authService, gamerRepository)

    configureRouting(authService, gamerRepository)
}