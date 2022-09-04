package com.thesohelshaikh.ytanalyser.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDetailResponse(
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
        @SerialName("etag")
        val etag: String?,
        @SerialName("id")
        val id: String?,
        @SerialName("kind")
        val kind: String?,
        @SerialName("snippet")
        val snippet: Snippet?
    ) {
        @Serializable
        data class Snippet(
            @SerialName("channelId")
            val channelId: String?,
            @SerialName("channelTitle")
            val channelTitle: String?,
            @SerialName("description")
            val description: String?,
            @SerialName("localized")
            val localized: Localized? = null,
            @SerialName("publishedAt")
            val publishedAt: String?,
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
    }

    @Serializable
    data class PageInfo(
        @SerialName("resultsPerPage")
        val resultsPerPage: Int?,
        @SerialName("totalResults")
        val totalResults: Int?
    )
}