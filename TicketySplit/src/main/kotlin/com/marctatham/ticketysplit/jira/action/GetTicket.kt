package com.marctatham.ticketysplit.jira.action

import com.marctatham.ticketysplit.jira.client
import com.marctatham.ticketysplit.jira.data.JiraTicket
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun getJiraTicketDetails(ticketId: String): JiraTicket {
    val response: JiraTicket = client().use {
        it.get("https://dowjones.atlassian.net/rest/api/3/issue/$ticketId").body()
    }

    return response
}