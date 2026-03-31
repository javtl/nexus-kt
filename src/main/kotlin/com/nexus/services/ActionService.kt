package com.nexus.services

import com.nexus.models.ActionRequest
import com.nexus.models.ActionResponse
import com.nexus.models.ActionType
import io.ktor.client.*
import org.slf4j.LoggerFactory
import java.util.*

/**
 * ActionService: El "Músculo" del Action Engine (NX-203).
 * Orquesta la ejecución de comandos de escritura autorizados en el Factory Engine.
 */
class ActionService(
    private val authService: AuthService,
    private val client: HttpClient // Lo usaremos para las llamadas reales a Steam/Discord
) {
    private val logger = LoggerFactory.getLogger(ActionService::class.java)

    /**
     * executeAction: El punto donde la intención de la IA se convierte en realidad.
     */
    suspend fun executeAction(request: ActionRequest): ActionResponse {
        logger.info("🚀 [Nexus Quant] Procesando: ${request.actionType} -> Target: ${request.targetId}")

        // 1. Recuperar el Token del Vault
        // OJO: fetchAgentToken devuelve el JSON. En el futuro deberíamos parsearlo.
        val rawTokenResponse = authService.fetchAgentToken()

        if (rawTokenResponse == null) {
            logger.error("❌ [Nexus Quant] Error crítico: Vault (Auth0) no devolvió credenciales.")
            return ActionResponse(
                success = false,
                message = "Error de Autorización: Vault inaccesible."
            )
        }

        // 2. Ruteo de ejecución (Estrategia de la Factoría)
        return try {
            when (request.actionType) {
                ActionType.MARK_COMPLETED -> handleMarkAsCompleted(request.targetId, rawTokenResponse)
                ActionType.EXECUTE_TRANSFER -> handleFintechTransfer(request, rawTokenResponse)
                else -> {
                    logger.warn("⚠️ [Nexus Quant] Acción no soportada: ${request.actionType}")
                    ActionResponse(false, "Acción reconocida pero no implementada.")
                }
            }
        } catch (e: Exception) {
            logger.error("💥 [Nexus Quant] Fallo en la ejecución: ${e.message}")
            ActionResponse(false, "Fallo en la ejecución de la acción.")
        }
    }

    /**
     * handleMarkAsCompleted: Lógica para Nexus Gaming.
     */
    private suspend fun handleMarkAsCompleted(gameId: String, token: String): ActionResponse {
        // Simulación de llamada a API externa (Steam/PlayStation)
        // Aquí usaríamos el 'client' para hacer un POST/PATCH real
        logger.info("🔗 [Nexus Quant] Conectando con API externa para actualizar: $gameId")

        val txId = UUID.randomUUID().toString()

        return ActionResponse(
            success = true,
            message = "Nexus ha marcado $gameId como completado.",
            transactionId = txId
        )
    }

    /**
     * handleFintechTransfer: Lógica para Stratos Ledger (ID: 1).
     */
    private fun handleFintechTransfer(request: ActionRequest, token: String): ActionResponse {
        val amount = request.payload["amount"] ?: "0.00"
        logger.info("💰 [Stratos Bridge] Preparando transferencia de $amount")

        return ActionResponse(
            success = true,
            message = "Orden de transferencia por $amount enviada a Stratos Ledger.",
            transactionId = UUID.randomUUID().toString()
        )
    }
}