package com.nexus.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Auth0TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int
)

