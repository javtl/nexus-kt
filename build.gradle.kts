plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    // Plugin necesario para que @Serializable funcione
    kotlin("plugin.serialization") version "2.0.0"
}

group = "com.nexus"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // 1. Servidor (Usando tu catálogo libs)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.auth)

    // 2. Extensiones de Servidor
    val ktor_v = "3.0.3"
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_v")
    implementation("io.ktor:ktor-server-sessions:$ktor_v")

    // 3. Cliente
    implementation("io.ktor:ktor-client-core:$ktor_v")
    implementation("io.ktor:ktor-client-apache:$ktor_v")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_v")

    // 4. Serialización
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_v")

    // 5. Logs
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // 6. BASE DE DATOS (¡La pieza que faltaba para la NX-103!)
    // Esta línea soluciona el error de "Unresolved reference mongodb"
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.3.1")

    // Tests
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}