package com.marctatham.ticketysplit.domain

import com.marctatham.ticketysplit.common.csv.parseCsvFile
import com.marctatham.ticketysplit.common.jira.data.*

import mu.KotlinLogging

private const val KEY = "Key"
private const val KEY_SUMMARY = "Summary"
private const val KEY_STORY_POINTS = "Story Points"
private const val KEY_DESC = "Description"
private const val KEY_PARENT = "parent"

private val logger = KotlinLogging.logger {}

fun bulkCreateTickets(filePath: String) {
    // NOTE: each map is a row in the CSV file
    val parsedCsv: List<Map<String, String>> = parseCsvFile(filePath)
    val withKey = parsedCsv.filter { it: Map<String, String> -> it.key().isNotBlank() }

    logger.info { "withKey" }
    withKey.forEach {
        logger.debug { it }
    }

    logger.info { "\n" }

    val withoutKey = parsedCsv.filter { it: Map<String, String> -> it.key().isBlank() }
    logger.info { "withoutKey" }
    withoutKey.forEach {
        logger.debug { it }
    }

    val jiraTickets: List<CreateTicket> = withoutKey.map { it: Map<String, String> ->
        mapToJiraTicket(it)
    }

    // TODO: let's test this one:
    val ticket = jiraTickets.first()
    logger.info { "ticket: $ticket" }
}


fun mapToJiraTicket(
    row: Map<String, String>,
): CreateTicket {
    val summary = row[KEY_SUMMARY] ?: error("Summary is required")
    val parentKey = row[KEY_PARENT] ?: error("Summary is required")
    val description = row[KEY_DESC] ?: ""
    val storyPoints = row[KEY_STORY_POINTS]?.toDoubleOrNull()
    val labels = listOf("bcn_automation")

    return CreateTicket(
        fields = Fields(
            summary = summary,
            description = Description(
                version = 1,
                type = "doc", // NOTE: don't support much in the form of rich content right now
                content = listOf(
                    Content(
                        type = "paragraph",
                        listOf(
                            ContentDetail(type = "text", text = description)
                        )
                    ),
                )
            ),
            labels = labels,
            storyPoints = storyPoints,
            parent = Parent(parentKey),
        )
    )
}

private fun Map<String, String>.key() = this[KEY] ?: ""