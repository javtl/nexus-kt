package com.nexus.routes

import com.nexus.models.ActionRequest
import com.nexus.services.ActionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ActionRoutes")

/**
 * ActionRoutes: El endpoint de ejecución de Nexus Quant (ID: H).
 */
fun Route.actionRouting(actionService: ActionService) {
    route("/api/v1/action") {
        post {
            try {
                // 1. Recibir la intención de la IA
                // Ktor usará la serialización que configuramos en Application.kt
                val request = call.receive<ActionRequest>()

                logger.info("⚡ Nexus Quant recibiendo orden: ${request.actionType} para ${request.targetId}")

                // 2. Ejecutar a través del ActionService (The Muscle)
                val response = actionService.executeAction(request)

                // 3. Respuesta estructurada
                if (response.success) {
                    logger.info("✅ Acción completada: ${response.transactionId}")
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    logger.warn("⚠️ Acción fallida: ${response.message}")
                    call.respond(HttpStatusCode.BadRequest, response)
                }

            } catch (e: io.ktor.serialization.JsonConvertException) {
                // Error específico si la IA manda un JSON mal formado
                logger.error("❌ Error de formato JSON: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Formato de acción inválido"))
            } catch (e: Exception) {
                logger.error("💥 Error crítico en Action Engine: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error interno en Nexus Quant")
                )
            }
        }
    }
}