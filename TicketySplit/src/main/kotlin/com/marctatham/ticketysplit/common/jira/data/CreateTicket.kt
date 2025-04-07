package com.marctatham.ticketysplit.common.jira.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateTicket(
    val fields: Fields
)