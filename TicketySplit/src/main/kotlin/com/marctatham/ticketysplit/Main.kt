package com.marctatham.ticketysplit

import com.marctatham.ticketysplit.domain.bulkCreateTickets
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = runBlocking {

    // let's query a ticket status to validate that it's all setup
//    val ticket = getJiraTicketDetails("DJMA-5506")
    logger.info { "TicketySplit init" }

    // todo: parse the CSV file
    // todo: create a ticket for each row
    val filePath = "/Users/marctatham/Downloads/taskbreakdown.csv"
    bulkCreateTickets(filePath)

    logger.info { "TicketySplit complete" }
}


// simple utility extension to grab the simple string argument
private fun Map<String, List<String>>.getStringArgumentValue(key: String): String = (this[key] ?: emptyList()).first()