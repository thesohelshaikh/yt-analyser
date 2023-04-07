package com.thesohelshaikh.ytanalyser.data.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistVideoDetailResponse(
    @SerialName("items")
    val items: List<Item?>?
) {
    @Serializable
    data class Item(
        @SerialName("contentDetails")
        val contentDetails: ContentDetails?
    ) {
        @Serializable
        data class ContentDetails(
            @SerialName("duration")
            val duration: String?
        )
    }
}