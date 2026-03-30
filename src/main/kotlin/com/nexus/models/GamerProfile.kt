package com.nexus.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class GamerProfile(
    @BsonId val id: String, // ID de Auth0
    val username: String,
    val email: String,
    val level: Int = 1,
    val experience: Long = 0
)