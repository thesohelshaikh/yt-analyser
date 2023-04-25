package com.thesohelshaikh.ytanalyser.data.network.model


import com.thesohelshaikh.ytanalyser.data.local.entities.VideoContentDetailsEntity
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoSnippetEntity
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoStatisticsEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDetailResponse(
        @SerialName("etag")
    val etag: String?,
        @SerialName("items")
    val items: List<VideoItem?>?,
        @SerialName("kind")
    val kind: String?,
        @SerialName("pageInfo")
    val pageInfo: PageInfo?
) {
    @Serializable
    data class VideoItem(
            @SerialName("contentDetails")
        val contentDetails: ContentDetails?,
            @SerialName("etag")
        val etag: String?,
            @SerialName("id")
        val id: String,
            @SerialName("kind")
        val kind: String?,
            @SerialName("snippet")
        val snippet: Snippet?,
            @SerialName("statistics")
        val statistics: Statistics?
    ) {
        @Serializable
        data class ContentDetails(
                @SerialName("caption")
            val caption: String?,
                @SerialName("contentRating")
            val contentRating: ContentRating?,
                @SerialName("definition")
            val definition: String?,
                @SerialName("dimension")
            val dimension: String?,
                @SerialName("duration")
            val duration: String?,
                @SerialName("licensedContent")
            val licensedContent: Boolean?,
                @SerialName("projection")
            val projection: String?
        ) {
            @Serializable
            class ContentRating
        }

        @Serializable
        data class Snippet(
                @SerialName("categoryId")
            val categoryId: String?,
                @SerialName("channelId")
            val channelId: String?,
                @SerialName("channelTitle")
            val channelTitle: String?,
                @SerialName("defaultAudioLanguage")
            val defaultAudioLanguage: String? = null,
                @SerialName("defaultLanguage")
            val defaultLanguage: String? = null,
                @SerialName("description")
            val description: String?,
                @SerialName("liveBroadcastContent")
            val liveBroadcastContent: String?,
                @SerialName("localized")
            val localized: Localized?,
                @SerialName("publishedAt")
            val publishedAt: String?,
                @SerialName("tags")
            val tags: List<String?>? = null,
                @SerialName("thumbnails")
            val thumbnails: Thumbnails?,
                @SerialName("title")
            val title: String?
        ) {
            @Serializable
            data class Localized(
                @SerialName("description")
                val description: String?,
                @SerialName("title")
                val title: String?
            )
        }

        @Serializable
        data class Statistics(
            @SerialName("commentCount")
            val commentCount: String? = null,
            @SerialName("favoriteCount")
            val favoriteCount: String?,
            @SerialName("likeCount")
            val likeCount: String?,
            @SerialName("viewCount")
            val viewCount: String?
        )
    }

    @Serializable
    data class PageInfo(
        @SerialName("resultsPerPage")
        val resultsPerPage: Int?,
        @SerialName("totalResults")
        val totalResults: Int?
    )
}

fun VideoDetailResponse.VideoItem.asEntity() = VideoEntity(
        id = id,
        etag = etag,
        kind = kind,
        contentDetails = contentDetails?.asEntity(),
        snippet = snippet?.asEntity(),
        statistics = statistics?.asEntity()
)

fun VideoDetailResponse.VideoItem.ContentDetails.asEntity() = VideoContentDetailsEntity(
        caption = caption,
        definition = definition,
        dimension = dimension,
        duration = duration,
        licensedContent = licensedContent,
        projection = projection
)

fun VideoDetailResponse.VideoItem.Snippet.Localized.asEntity() = VideoSnippetEntity.LocalizedEntity(
        description = description,
        title = title)
fun VideoDetailResponse.VideoItem.Snippet.asEntity() = VideoSnippetEntity(
        categoryId = categoryId,
        channelId = channelId,
        channelTitle = channelTitle,
        defaultAudioLanguage = defaultAudioLanguage,
        defaultLanguage = defaultLanguage,
        description = description,
        liveBroadcastContent = liveBroadcastContent,
        localized = localized?.asEntity(),
        publishedAt = publishedAt,
        tags = tags,
        thumbnail = thumbnails?.getThumbnailUrl(),
        title = title
)

fun VideoDetailResponse.VideoItem.Statistics.asEntity() = VideoStatisticsEntity(
        commentCount = commentCount,
        favoriteCount = favoriteCount,
        likeCount = likeCount,
        viewCount = viewCount
)