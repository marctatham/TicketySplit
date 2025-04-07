package com.marctatham.ticketysplit.jira.data

import kotlinx.serialization.Serializable

@Serializable
data class JiraTicket(
    val expand: String,
    val id: String,
    val key: String,
    val fields: Fields
)