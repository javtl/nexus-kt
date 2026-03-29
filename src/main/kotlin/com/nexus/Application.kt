package com.nexus

import com.nexus.plugins.*
import com.nexus.database.DatabaseFactory
import com.nexus.repository.GamerRepository
import com.nexus.services.AuthService
import io.ktor.server.application.*

// Función principal que arranca el motor Netty
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    // 1. CONEXIÓN A BASE DE DATOS
    // Inicializamos y guardamos la referencia de la DB
    val db = DatabaseFactory.init(environment)

    // 2. INSTANCIACIÓN DEL REPOSITORIO
    // Creamos la herramienta 'gamerRepo' usando el molde 'GamerRepository'
    val gamerRepo = GamerRepository(db)

    // 3. CONFIGURACIÓN DE PLUGINS DE SISTEMA
    // Importante: Serialization antes que Routing para que entienda JSON
    configureSerialization()
    configureMonitoring()

    // 4. CONFIGURACIÓN DE SEGURIDAD Y HTTP
    val httpClient = configureHttpClient()
    val authService = AuthService(httpClient, environment)

    // Instalamos el plugin de JWT
    configureSecurity()

    // 5. CONFIGURACIÓN DE RUTAS
    // Pasamos el servicio de Auth y el repositorio de Gamers a las rutas
    configureRouting(authService, gamerRepo)
}