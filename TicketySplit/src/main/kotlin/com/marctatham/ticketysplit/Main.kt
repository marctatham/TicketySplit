package com.marctatham.ticketysplit

import com.marctatham.ticketysplit.jira.data.JiraTicket
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = runBlocking {
    logger.info { ("Hello, World! $") }

    // let's query a ticket status to validate that it's all setup
    // todo: fetch from local properties or similar
    val ticket = getJiraTicketDetails("DJMA-5836", "")
    logger.info { "ticket: $ticket" }

}

suspend fun getJiraTicketDetails(ticketId: String, apiToken: String): JiraTicket {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(Logging) { level = LogLevel.ALL }
    }

    val response: JiraTicket = client.use {
        it.get("https://dowjones.atlassian.net/rest/api/3/issue/$ticketId") {
            headers {
                append("Authorization", "Basic $apiToken")
                append("Accept", "application/json")
            }
        }.body()
    }





    return response
}

// simple utility extension to grab the simple string argument
private fun Map<String, List<String>>.getStringArgumentValue(key: String): String = (this[key] ?: emptyList()).first()