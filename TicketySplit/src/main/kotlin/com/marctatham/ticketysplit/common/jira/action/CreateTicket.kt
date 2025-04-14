package com.marctatham.ticketysplit.common.jira.action

import com.marctatham.ticketysplit.common.jira.client
import com.marctatham.ticketysplit.common.jira.data.CreateTicket
import com.marctatham.ticketysplit.common.jira.data.JiraTicket
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun createTicket(
    ticket: CreateTicket
): Unit {


    val response: HttpResponse = client().use {
        it.post("https://dowjones.atlassian.net/rest/api/3/issue") {
            setBody(ticket)
        }
    }

    if (response.status.value != 201) {
        throw Exception("Failed to create ticket: \n${ticket.fields.summary} \n${response.status}")
    }
}