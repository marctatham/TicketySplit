package com.marctatham.ticketysplit.jira.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateTicket(
    val fields: Fields
)