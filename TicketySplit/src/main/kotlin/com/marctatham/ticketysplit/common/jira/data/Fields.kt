package com.marctatham.ticketysplit.common.jira.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val summary: String,
    val description: Description? = null,
    val labels: List<String>,
    @SerialName("customfield_10105") val storyPoints: Double? = null,
    val parent: Parent? = null,
    val project: Project? = null,
    val issueType: IssueType? = null,
    val components: List<Component>? = emptyList(),
)

@Serializable
data class Parent(val key: String?)

@Serializable
data class Project(val id: String?)

@Serializable
data class IssueType(val id: String?)

@Serializable
data class Component(val id: String?)

@Serializable
data class Description(
    val type: String,
    val version: Int,
    val content: List<Content>
)

@Serializable
data class Content(
    val type: String,
    val content: List<ContentDetail>,
    val attrs: Attrs? = null
)

@Serializable
data class ContentDetail(
    val type: String,
    val text: String? = null,
    val attrs: Attrs? = null
)

@Serializable
data class Attrs(
    val url: String? = null
)