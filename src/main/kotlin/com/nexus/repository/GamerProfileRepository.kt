package com.nexus.repository

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.nexus.database.DatabaseFactory
import com.nexus.models.GamerProfile
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

// Recomendado: Renombrar a GamerProfileRepository para que coincida con el Modelo
class GamerProfileRepository(private val db: MongoDatabase) {

    private val collection by lazy {
        // Usamos la base de datos inyectada (db) en lugar de llamar a DatabaseFactory directamente
        db.getCollection<GamerProfile>("gamers")
    }

    /**
     * NX-301: Cuenta el total de perfiles en la base de datos.
     * Crucial para las estadísticas del Dashboard.
     */
    suspend fun countProfiles(): Long {
        return collection.countDocuments()
    }

    /**
     * Guarda o actualiza un perfil de jugador (Upsert).
     */
    suspend fun saveProfile(profile: GamerProfile) {
        collection.replaceOne(
            eq("_id", profile.id),
            profile,
            ReplaceOptions().upsert(true)
        )
    }

    /**
     * Busca un jugador por su ID único (el 'sub' de Auth0).
     */
    suspend fun getProfile(id: String): GamerProfile? {
        return collection.find(eq("_id", id)).firstOrNull()
    }

    suspend fun getAllProfiles(): List<GamerProfile> {
        return collection.find().toList()
    }
}