package com.thesohelshaikh.ytanalyser.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thesohelshaikh.ytanalyser.data.network.model.Thumbnails

@Entity("videos")
data class VideoEntity(
        @PrimaryKey
        @ColumnInfo("id")
        val id: String,
        @ColumnInfo("etag")
        val etag: String?,
        @ColumnInfo("kind")
        val kind: String?,
        @Embedded("contentDetails_")
        val contentDetails: VideoContentDetailsEntity?,
        @Embedded("snippet_")
        val snippet: VideoSnippetEntity?,
        @Embedded("statistics_")
        val statistics: VideoStatisticsEntity?
)

data class VideoSnippetEntity(
        @ColumnInfo("categoryId")
        val categoryId: String?,
        @ColumnInfo("channelId")
        val channelId: String?,
        @ColumnInfo("channelTitle")
        val channelTitle: String?,
        @ColumnInfo("defaultAudioLanguage")
        val defaultAudioLanguage: String? = null,
        @ColumnInfo("defaultLanguage")
        val defaultLanguage: String? = null,
        @ColumnInfo("description")
        val description: String?,
        @ColumnInfo("liveBroadcastContent")
        val liveBroadcastContent: String?,
        @Embedded("localized_")
        val localized: LocalizedEntity?,
        @ColumnInfo("publishedAt")
        val publishedAt: String?,
        @ColumnInfo("tags")
        val tags: List<String?>?,
        @ColumnInfo("thumbnail")
        val thumbnail: String?,
        @ColumnInfo("title")
        val title: String?
) {
    data class LocalizedEntity(
            @ColumnInfo("description")
            val description: String?,
            @ColumnInfo("title")
            val title: String?
    )
}

data class VideoContentDetailsEntity(
        @ColumnInfo("caption")
        val caption: String?,
        @ColumnInfo("definition")
        val definition: String?,
        @ColumnInfo("dimension")
        val dimension: String?,
        @ColumnInfo("duration")
        val duration: String?,
        @ColumnInfo("licensedContent")
        val licensedContent: Boolean?,
        @ColumnInfo("projection")
        val projection: String?
)

data class VideoStatisticsEntity(
        @ColumnInfo("commentCount")
        val commentCount: String?,
        @ColumnInfo("favoriteCount")
        val favoriteCount: String?,
        @ColumnInfo("likeCount")
        val likeCount: String?,
        @ColumnInfo("viewCount")
        val viewCount: String?
)