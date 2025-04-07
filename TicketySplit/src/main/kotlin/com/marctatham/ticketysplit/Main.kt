package com.marctatham.ticketysplit

import com.marctatham.ticketysplit.jira.action.getJiraTicketDetails
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = runBlocking {
    logger.info { ("Hello, World! $") }

    // let's query a ticket status to validate that it's all setup
    val ticket = getJiraTicketDetails("DJMA-5506")
    logger.info { "ticket: $ticket" }

}



// simple utility extension to grab the simple string argument
private fun Map<String, List<String>>.getStringArgumentValue(key: String): String = (this[key] ?: emptyList()).first()