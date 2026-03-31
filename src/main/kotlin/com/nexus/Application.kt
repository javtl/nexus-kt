package com.nexus

import com.nexus.plugins.*
import com.nexus.database.DatabaseFactory
import com.nexus.repository.GamerRepository
import com.nexus.services.ActionService // <--- NUEVO IMPORT
import com.nexus.services.AuthService
import io.ktor.server.application.*

// Función principal que arranca el motor Netty
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    // 1. CONEXIÓN A BASE DE DATOS
    // Inicializamos y guardamos la referencia de la DB (MongoDB/Postgres)
    val db = DatabaseFactory.init(environment)

    // 2. INSTANCIACIÓN DE REPOSITORIOS
    // Herramienta para persistir perfiles de la Factoría
    val gamerRepo = GamerRepository(db)

    // 3. CONFIGURACIÓN DE PLUGINS DE SISTEMA
    // Serialization es CRÍTICO antes de Routing para procesar JSON
    configureSerialization()
    configureMonitoring()

    // 4. CAPA DE SERVICIOS (Nexus Quant - ID: H)
    val httpClient = configureHttpClient()

    // El "Cerebro": Gestiona tokens M2M con el Vault (Auth0)
    val authService = AuthService(httpClient, environment)

    // El "Músculo": El Action Engine que ejecuta órdenes de la IA
    val actionService = ActionService(authService)

    // 5. SEGURIDAD
    // Configura el verificador de JWT para proteger los endpoints
    configureSecurity()

    // 6. CONFIGURACIÓN DE RUTAS (El Enchufado Final)
    // Inyectamos authService, gamerRepo y el nuevo actionService
    configureRouting(authService, gamerRepo, actionService)
}