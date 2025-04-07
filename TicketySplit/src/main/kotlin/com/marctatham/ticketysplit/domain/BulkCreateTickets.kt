package com.marctatham.ticketysplit.domain

import com.marctatham.ticketysplit.common.csv.parseCsvFile
import com.marctatham.ticketysplit.common.jira.action.createTicket
import com.marctatham.ticketysplit.common.jira.data.*

import mu.KotlinLogging

private const val KEY = "Key"
private const val KEY_SUMMARY = "Summary"
private const val KEY_STORY_POINTS = "Story Points"
private const val KEY_DESC = "Description"
private const val KEY_PARENT = "parent"
private const val KEY_ISSUE_TYPE = "Issue Type"

private const val VAL_PROJECT_DJMA = "15781" // DJMA project

private val logger = KotlinLogging.logger {}

suspend fun bulkCreateTickets(filePath: String) {
    // NOTE: each map is a row in the CSV file
    val parsedCsv: List<Map<String, String>> = parseCsvFile(filePath)
    val withKey = parsedCsv.filter { it: Map<String, String> -> it.key().isNotBlank() }

    logger.warn { "items withKey will be skipped!" }
    withKey.forEach {
        logger.debug { it }
    }

    logger.info { "\n" }

    val withoutKey = parsedCsv.filter { it: Map<String, String> -> it.key().isBlank() }
    logger.info { "items withoutKey will be created" }
    withoutKey.forEach {
        logger.debug { it }
    }

    val jiraTickets: List<CreateTicket> = withoutKey.map { it: Map<String, String> ->
        mapToJiraTicket(it)
    }

    // TODO: let's test this one:
    val ticket = jiraTickets.first()
    logger.info { "ticket: $ticket" }
    createTicket(ticket)
}


private fun mapToJiraTicket(
    row: Map<String, String>,
): CreateTicket {
    val summary = row[KEY_SUMMARY] ?: error("Summary is required")
    val parentKey = row[KEY_PARENT] ?: error("Summary is required")
    val description = row[KEY_DESC] ?: ""
    val storyPoints = row[KEY_STORY_POINTS]?.toDoubleOrNull()
    val issueTypeName = row[KEY_ISSUE_TYPE]?: error("Issue Type is required")
    val issueType = mapIssueType(issueTypeName)

    val labels = listOf("bcn_automation") // we need some way to track the ticket was created by this script.

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
            project = Project(VAL_PROJECT_DJMA),
            issueType = IssueType(issueType)
        )
    )
}

private fun mapIssueType(issueType: String): String {
    return when (issueType) {
        "Spike" -> JiraIssueType.SPIKE
        "Bug" -> JiraIssueType.BUG
        "Task" -> JiraIssueType.TASK
        "Epic" -> JiraIssueType.EPIC
        "Story" -> JiraIssueType.STORY
        else -> error("Unknown issue type: $issueType")
    }
}

private fun Map<String, String>.key() = this[KEY] ?: ""