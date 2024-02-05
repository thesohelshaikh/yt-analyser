package com.thesohelshaikh.ytanalyser.data.repository

import com.thesohelshaikh.ytanalyser.data.network.YoutubeService
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.VideoDetailResponse
import javax.inject.Inject


class YoutubeRepository @Inject constructor(
    private val youtubeService: YoutubeService
) {

    suspend fun getVideoDetails(videoId: String): VideoDetailResponse {
        return youtubeService.getVideoDetails(videoId)
    }

    suspend fun getPlaylistDetails(
        playlistId: String
    ): PlaylistDetailResponse {
        return youtubeService.getPlaylistDetails(playlistId)
    }

    suspend fun getPlaylistVideoIds(
        playlistId: String,
        pageToken: String?
    ): PlaylistVideoIdResponse {
        return youtubeService.getPlaylistVideoIds(playlistId = playlistId, pageToken = pageToken)
    }

    suspend fun getPlaylistVideoDetails(
        videoId: String
    ): PlaylistVideoDetailResponse {
        return youtubeService.getPlaylistVideoDetails(videoId)
    }
}