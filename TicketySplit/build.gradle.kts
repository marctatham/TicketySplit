plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    application
}

group = "com.marctatham.ticketysplit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.jackson)

    implementation(libs.logback.classic)
    implementation(libs.microutils.logging)

    implementation(libs.kotlincsv)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.marctatham.ticketysplit.MainKt")
}