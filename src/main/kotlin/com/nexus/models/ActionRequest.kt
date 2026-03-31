package com.nexus.models

import kotlinx.serialization.Serializable

/**
 * ActionRequest: El contrato de ejecución de Nexus Quant (ID: H).
 * Define cómo la IA solicita una acción de escritura en el ecosistema.
 */
@Serializable
data class ActionRequest(
    /**
     * El tipo de operación a realizar.
     * Determina la lógica de ruteo en el ActionEngine.
     */
    val actionType: ActionType,

    /**
     * El identificador del recurso sobre el que se actúa.
     * Ejemplos: "steam_app_12345", "iban_revolut_001", "habit_id_99".
     */
    val targetId: String,

    /**
     * Datos adicionales necesarios para la acción.
     * Ejemplo: MapOf("amount" to "50.00", "currency" to "EUR").
     */
    val payload: Map<String, String> = emptyMap()
)

/**
 * ActionType: Los verbos de acción del ecosistema Factory.
 * Permite que Nexus sea escalable a diferentes verticales (Gaming, Fintech, Business).
 */
@Serializable
enum class ActionType {
    // Nexus Gaming (Hackathon Current Focus)
    MARK_COMPLETED,
    UPDATE_PROGRESS,

    // Nexus Finance / Stratos Ledger (ID: 1)
    EXECUTE_TRANSFER,
    FREEZE_ACCOUNT,

    // The Brick (ID: 9) / General Productivity
    LOG_HABIT_SUCCESS,

    // Nexus Business / Holding Management
    REVOKE_ACCESS
}

/**
 * ActionResponse: La confirmación de ejecución que se devuelve a la IA.
 */
@Serializable
data class ActionResponse(
    val success: Boolean,
    val message: String,
    val transactionId: String? = null
)