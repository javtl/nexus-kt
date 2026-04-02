package com.nexus.services

import com.nexus.models.ActionRequest
import com.nexus.models.ActionResponse
import com.nexus.models.ActionType
import java.util.*

class ActionService(private val authService: AuthService) {

    suspend fun executeAction(request: ActionRequest, accessToken: String?): ActionResponse {
        // 1. Identify Sensitive Actions
        if (request.actionType.requiresStepUp()) {
            val isMfaVerified = verifyMfaStatus(accessToken)
            if (!isMfaVerified) {
                return ActionResponse(
                    success = false,
                    message = "STEP_UP_REQUIRED: This action requires Multi-Factor Authentication.",
                    transactionId = "SEC-ERR-${UUID.randomUUID().toString().take(8)}"
                )
            }
        }

        // 2. Logic Routing (What we did yesterday)
        return try {
            when (request.actionType) {
                ActionType.MARK_COMPLETED -> handleMarkAsCompleted(request.targetId)
                ActionType.EXECUTE_TRANSFER -> handleTransfer(request.targetId, request.payload)
                // ... rest of the cases
                else -> ActionResponse(false, "Action ${request.actionType} not yet implemented", null)
            }
        } catch (e: Exception) {
            ActionResponse(false, "Execution failed: ${e.message}", null)
        }
    }

    /**
     * verifyMfaStatus: Decodes the JWT to check the 'amr' claim.
     * Technical Note: 'amr' (Authentication Methods Reference) should contain 'mfa'.
     */
    private fun verifyMfaStatus(token: String?): Boolean {
        if (token == null) return false

        // TODO: In a real scenario, use JWT.decode(token).getClaim("amr").asList(String::class.java)
        // For the Step-up Challenge (NX-204) POC, we simulate the check:
        println("Checking Step-up status for token: ${token.take(10)}...")

        // Return false to trigger the Step-up challenge in our tests
        return false
    }

    private fun handleMarkAsCompleted(targetId: String): ActionResponse {
        return ActionResponse(true, "Progress updated for $targetId", UUID.randomUUID().toString())
    }

    private fun handleTransfer(targetId: String, payload: Map<String, String>): ActionResponse {
        val amount = payload["amount"] ?: "0"
        return ActionResponse(true, "Transferred $amount to $targetId", UUID.randomUUID().toString())
    }
}