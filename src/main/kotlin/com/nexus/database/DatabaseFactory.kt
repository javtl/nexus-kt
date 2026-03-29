package com.nexus.database

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*

object DatabaseFactory {
    private lateinit var client: MongoClient
    lateinit var db: MongoDatabase


    fun init(environment: ApplicationEnvironment): MongoDatabase {
        val uri = environment.config.propertyOrNull("storage.uri")?.getString()
            ?: throw RuntimeException("CRÍTICO: No se encontró la MONGO_URI en las variables de entorno.")

        client = MongoClient.create(uri)
        db = client.getDatabase("nexus_db")

        println("Connected to MongoDB Atlas successfully! 🍃")

        return db
    }
}