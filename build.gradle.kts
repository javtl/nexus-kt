plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    // Asegúrate de que este plugin de serialización esté aquí si vas a usar JSON
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
// 1. Servidor (Usando tu catálogo libs si funciona, o forzando si falla)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.auth)

    // 2. Extensiones de Servidor (Nombres corregidos para Ktor 3)
    val ktor_v = "3.0.3"
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_v")
    implementation("io.ktor:ktor-server-sessions:$ktor_v")

    // 3. Cliente (Nombres corregidos para Ktor 3)
    implementation("io.ktor:ktor-client-core:$ktor_v")
    implementation("io.ktor:ktor-client-apache:$ktor_v")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_v")

    // 4. Serialización
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_v")

    // 5. Logs Seguros
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}