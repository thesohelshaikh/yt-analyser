package com.thesohelshaikh.ytanalyser.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDetailResponse(
    @SerialName("etag")
    val etag: String?,
    @SerialName("items")
    val items: List<Item?>?,
    @SerialName("kind")
    val kind: String?,
    @SerialName("pageInfo")
    val pageInfo: PageInfo?
) {
    @Serializable
    data class Item(
        @SerialName("contentDetails")
        val contentDetails: ContentDetails?,
        @SerialName("etag")
        val etag: String?,
        @SerialName("id")
        val id: String?,
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
            val tags: List<String?>?,
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
            val commentCount: String?,
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