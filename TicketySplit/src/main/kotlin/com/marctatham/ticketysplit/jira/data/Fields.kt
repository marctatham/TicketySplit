package com.marctatham.ticketysplit.jira.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val summary: String,
    val description: Description? = null,
    val labels: List<String>,
    @SerialName("customfield_10105") val storyPoints: Double? = null,
    val parent: Parent? = null
)

@Serializable
data class Parent(
    val key: String?,
)

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