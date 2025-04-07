package com.marctatham.ticketysplit.common.jira

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.*

val email = System.getenv("JIRA_USERNAME") ?: error("JIRA_USERNAME environment variable not set")
val apiToken = System.getenv("JIRA_API_TOKEN") ?: error("JIRA_API_TOKEN environment variable not set")
val authString = "$email:$apiToken"
val encodedAuth: String = Base64.getEncoder().encodeToString(authString.toByteArray())

fun client(): HttpClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        install(Logging) { level = LogLevel.ALL }
        defaultRequest {
            header(HttpHeaders.Authorization, "Basic $encodedAuth")
            header(HttpHeaders.Accept, "application/json")
        }
    }

    return client
}