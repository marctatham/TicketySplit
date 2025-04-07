package com.marctatham.ticketysplit

import com.marctatham.ticketysplit.jira.data.JiraTicket
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.*


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = runBlocking {
    logger.info { ("Hello, World! $") }

    // let's query a ticket status to validate that it's all setup
    val ticket = getJiraTicketDetails("DJMA-5836")
    logger.info { "ticket: $ticket" }

}

suspend fun getJiraTicketDetails(ticketId: String): JiraTicket {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(Logging) { level = LogLevel.ALL }
    }

    val email = System.getenv("JIRA_USERNAME") ?: error("JIRA_USERNAME environment variable not set")
    val apiToken = System.getenv("JIRA_API_TOKEN") ?: error("JIRA_API_TOKEN environment variable not set")
    val authString = "$email:$apiToken"
    val encodedAuth = Base64.getEncoder().encodeToString(authString.toByteArray())
    logger.info { "with token: $encodedAuth" }

    val response: JiraTicket = client.use {
        it.get("https://dowjones.atlassian.net/rest/api/3/issue/$ticketId") {
            headers {
                append(HttpHeaders.Authorization, "Basic $encodedAuth")
                append(HttpHeaders.Accept, "application/json")
            }
        }.body()
    }





    return response
}

// simple utility extension to grab the simple string argument
private fun Map<String, List<String>>.getStringArgumentValue(key: String): String = (this[key] ?: emptyList()).first()