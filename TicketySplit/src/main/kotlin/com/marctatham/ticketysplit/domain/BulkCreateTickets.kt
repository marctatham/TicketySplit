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

    // filter out unhealthy entries (unhealthy entries will be logged to console
    val healthyRows: List<Map<String, String>> = filterHealthyEntries(parsedCsv)

    val jiraTickets: List<CreateTicket> = healthyRows.map { mapToJiraTicket(it) }

    logger.info { "HEALTHY ROWS:" }
    logger.info { "============================================================" }
    jiraTickets.forEach { logger.info { "${it.fields.issueType} - ${it.fields.summary}" } }

    // TODO: let's test just one for now:
    val ticket = jiraTickets.first()
    logger.info { "ticket: $ticket" }
    createTicket(ticket)
}

private fun filterHealthyEntries(rows: List<Map<String, String>>): List<Map<String, String>> {
    val rowsWithoutSummary: List<Map<String, String>> = rows.filter { row -> row[KEY_SUMMARY]?.isBlank() ?: true }
    val rowsWithKeys = rows.filter { row -> row.key().isNotBlank() }
    val rowsWithoutIssueType = rows.filter { row -> (row[KEY_ISSUE_TYPE] ?: "").isBlank() }
    val rowsHealthy = rows.filter {
        !rowsWithKeys.contains(it)
                && !rowsWithoutIssueType.contains(it)
                && !rowsWithoutSummary.contains(it)
    }

    logger.info { "============================================================" }
    logger.info { "Ticket Breakdown analysis:" }
    logger.info { "healthy: [${rowsHealthy.size}]" }
    logger.info { "ERROR:\t\trowsWithoutSummary: [${rowsWithoutSummary.size}]" }
    logger.info { "ERROR:\t\trowsWithKeys: [${rowsWithKeys.size}]" }
    logger.info { "ERROR:\t\trowsWithoutIssueType: [${rowsWithoutIssueType.size}]" }
    logger.info { "============================================================" }

    // TODO: useful for debugging dodgy data in the source CSV/Spreadsheet
//    rowsWithKeys.debugFilteredRows("Already has key: ")
//    rowsWithoutSummary.debugFilteredRows("NO SUMMARY: ")
//    rowsWithoutIssueType.debugFilteredRows("MISSING ISSUE TYPE: ")
//    logger.info { "============================================================" }
    return rowsHealthy
}


private fun mapToJiraTicket(
    row: Map<String, String>,
): CreateTicket {
    val summary = row[KEY_SUMMARY] ?: error("Summary is required")
    val parentKey = row[KEY_PARENT] ?: error("Summary is required")
    val description = row[KEY_DESC] ?: ""
    val storyPoints = row[KEY_STORY_POINTS]?.toDoubleOrNull()
    val issueTypeName = row[KEY_ISSUE_TYPE] ?: error("Issue Type is required")
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


private fun List<Map<String, String>>.debugFilteredRows(reason: String) = this.forEach { row ->
    logger.warn { "$reason: $row" }
}