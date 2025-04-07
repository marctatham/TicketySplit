package com.marctatham.ticketysplit

import com.marctatham.ticketysplit.jira.client
import com.marctatham.ticketysplit.jira.data.JiraTicket
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = runBlocking {
    logger.info { ("Hello, World! $") }

    // let's query a ticket status to validate that it's all setup
    val ticket = getJiraTicketDetails("DJMA-5836")
    logger.info { "ticket: $ticket" }

}

suspend fun getJiraTicketDetails(ticketId: String): JiraTicket {
    val client = client()
    val response: JiraTicket = client.use {
        it.get("https://dowjones.atlassian.net/rest/api/3/issue/$ticketId").body()
    }

    return response
}

// simple utility extension to grab the simple string argument
private fun Map<String, List<String>>.getStringArgumentValue(key: String): String = (this[key] ?: emptyList()).first()