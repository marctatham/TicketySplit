package com.marctatham.ticketysplit.common.jira.data

import kotlinx.serialization.Serializable

@Serializable
data class JiraTicket(
    val id: String,
    val key: String,
    val fields: Fields
)