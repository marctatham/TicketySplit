package com.marctatham.ticketysplit.jira.action

import com.marctatham.ticketysplit.jira.client
import com.marctatham.ticketysplit.jira.data.JiraTicket
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun createTicket(

): Unit {

    // TODO: add basic field spuport
//    val response: JiraTicket = client().use {
//        it.get("https://dowjones.atlassian.net/rest/api/3/issue/$ticketId").body()
//    }
//
//    return response
}