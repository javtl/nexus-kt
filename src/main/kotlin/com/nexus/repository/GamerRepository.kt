package com.nexus.repository

import com.nexus.database.DatabaseFactory
import com.nexus.models.GamerProfile
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions

import kotlinx.coroutines.flow.firstOrNull

object GamerRepository {

    // Accedemos a la colección "gamers" usando el modelo GamerProfile
    private val collection by lazy {
        DatabaseFactory.db.getCollection<GamerProfile>("gamers")
    }

    /**
     * Guarda o actualiza un perfil de jugador.
     * Usamos 'replaceOne' con 'upsert(true)' para que:
     * - Si el ID existe, sobrescriba los datos.
     * - Si el ID NO existe, cree el documento nuevo.
     */
    suspend fun saveProfile(profile: GamerProfile) {
        collection.replaceOne(
            eq("_id", profile.id), // Filtro por ID de Auth0
            profile,               // Objeto a guardar
            ReplaceOptions().upsert(true) // ¡Crucial para evitar duplicados!
        )
    }

    /**
     * Busca un jugador por su ID único (el 'sub' de Auth0).
     */
    suspend fun getProfile(id: String): GamerProfile? {
        return collection.find(eq("_id", id)).firstOrNull()
    }
}

