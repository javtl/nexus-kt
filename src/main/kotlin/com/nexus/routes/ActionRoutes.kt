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
 * ActionRoutes: Execution endpoint for Nexus (ID: H).
 * Updated for NX-204: Supports JWT extraction for Step-up Challenge.
 */
fun Route.actionRouting(actionService: ActionService) {
    route("/api/v1/action") {
        post {
            try {
                // 1. Extract the JWT from the Authorization Header
                // Format: "Bearer <token>"
                val authHeader = call.request.header(HttpHeaders.Authorization)
                val token = authHeader?.removePrefix("Bearer ")?.trim()

                // 2. Receive the Action Intent
                val request = call.receive<ActionRequest>()
                logger.info("⚡ Nexus receiving intent: ${request.actionType} for ${request.targetId}")

                // 3. Execute through ActionService passing the token for MFA verification
                val response = actionService.executeAction(request, token)

                // 4. Structured Response Handling
                if (response.success) {
                    logger.info("✅ Action completed: ${response.transactionId}")
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    // Check if failure is due to Step-up MFA requirement
                    if (response.message.contains("STEP_UP_REQUIRED")) {
                        logger.warn("🛡️ Security Challenge Triggered: MFA Required for ${request.actionType}")
                        call.respond(HttpStatusCode.Forbidden, response) // 403 Forbidden
                    } else {
                        logger.warn("⚠️ Action failed: ${response.message}")
                        call.respond(HttpStatusCode.BadRequest, response) // 400 Bad Request
                    }
                }

            } catch (e: io.ktor.serialization.JsonConvertException) {
                logger.error("❌ JSON Format Error: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid action format"))
            } catch (e: Exception) {
                logger.error("💥 Critical Error in Action Engine: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Internal Error in Nexus Engine")
                )
            }
        }
    }
}