package com.thesohelshaikh.ytanalyser.model

class VideoModel {
    var id: String? = null
    var duration: String? = null
    var title: String? = null
    var thumbnailURL: String? = null
    var channelTitle: String? = null

    constructor() {}
    constructor(
        id: String?,
        duration: String?,
        title: String?,
        thumbnailURL: String?,
        channelTitle: String?
    ) {
        this.id = id
        this.duration = duration
        this.title = title
        this.thumbnailURL = thumbnailURL
        this.channelTitle = channelTitle
    }

    override fun toString(): String {
        return "VideoModel{" +
                "id='" + id + '\'' +
                ", duration='" + duration + '\'' +
                '}'
    }
}