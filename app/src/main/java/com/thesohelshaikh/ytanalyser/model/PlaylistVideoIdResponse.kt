package com.thesohelshaikh.ytanalyser.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistVideoIdResponse(
    @SerialName("items")
    val items: List<Item?>?,
    @SerialName("nextPageToken")
    val nextPageToken: String? = null
) {
    @Serializable
    data class Item(
        @SerialName("contentDetails")
        val contentDetails: ContentDetails?
    ) {
        @Serializable
        data class ContentDetails(
            @SerialName("videoId")
            val videoId: String?
        )
    }
}