package com.thesohelshaikh.ytanalyser.model

import java.util.ArrayList

class PlaylistModel {
    var id: String? = null
    var totalDuration: Long = 0
    var title: String? = null
    var thumbnailURL: String? = null
    var createdBy: String? = null
    var numberOfVideos = 0
    var videoIDs: ArrayList<String>? = null

    fun addDuration(duration: Long) {
        totalDuration += duration
    }

    override fun toString(): String {
        return "PlaylistModel{" +
                "id='" + id + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", title='" + title + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", numberOfVideos=" + numberOfVideos +
                ", videoIDs=" + videoIDs +
                '}'
    }
}