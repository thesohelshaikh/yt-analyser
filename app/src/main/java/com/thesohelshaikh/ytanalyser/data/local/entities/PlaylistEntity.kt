package com.thesohelshaikh.ytanalyser.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class PlayListEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("thumbnailUrl")
    val thumbnailUrl: String?,
    @ColumnInfo("title")
    val title: String?,
    @ColumnInfo("channelTitle")
    val channelTitle: String?,
    @ColumnInfo("duration")
    val duration: Long,
    @ColumnInfo("createdAt")
    val createdAt: Long = System.currentTimeMillis()
)