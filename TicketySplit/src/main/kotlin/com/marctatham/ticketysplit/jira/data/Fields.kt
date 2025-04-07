package com.marctatham.ticketysplit.jira.data

import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val summary: String,
    val description: Description?
)