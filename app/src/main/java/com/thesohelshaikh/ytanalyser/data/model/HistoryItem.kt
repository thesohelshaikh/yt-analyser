package com.thesohelshaikh.ytanalyser.data.model

data class HistoryItem(
    val id: String,
    val thumbnail: String?,
    val title: String,
    val channelTitle: String,
    val resourceType: ResourceType,
    val createdAt: Long,
)