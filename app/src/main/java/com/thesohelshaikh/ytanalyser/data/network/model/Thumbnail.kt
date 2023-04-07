package com.thesohelshaikh.ytanalyser.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
        @SerialName("default")
    val default: Default?,
        @SerialName("high")
    val high: High?,
        @SerialName("maxres")
    val maxres: Maxres? = null,
        @SerialName("medium")
    val medium: Medium?,
        @SerialName("standard")
    val standard: Standard? = null
) {
    @Serializable
    data class Default(
        @SerialName("height")
        val height: Int?,
        @SerialName("url")
        val url: String?,
        @SerialName("width")
        val width: Int?
    )

    @Serializable
    data class High(
        @SerialName("height")
        val height: Int?,
        @SerialName("url")
        val url: String?,
        @SerialName("width")
        val width: Int?
    )

    @Serializable
    data class Maxres(
        @SerialName("height")
        val height: Int?,
        @SerialName("url")
        val url: String?,
        @SerialName("width")
        val width: Int?
    )

    @Serializable
    data class Medium(
        @SerialName("height")
        val height: Int?,
        @SerialName("url")
        val url: String?,
        @SerialName("width")
        val width: Int?
    )

    @Serializable
    data class Standard(
        @SerialName("height")
        val height: Int?,
        @SerialName("url")
        val url: String?,
        @SerialName("width")
        val width: Int?
    )

    fun getThumbnailUrl(): String? {
        return if (this.maxres != null) {
            this.maxres.url
        } else if (this.standard != null) {
            this.standard.url
        } else if (this.high != null) {
            this.high.url
        } else {
            null
        }
    }
}