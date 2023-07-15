package com.thesohelshaikh.ytanalyser.data.network

import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.VideoDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {

    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") videoId: String,
        @Query("part") part: String = "contentDetails, snippet, statistics",
    ): VideoDetailResponse

    @GET("playlists")
    suspend fun getPlaylistDetails(
        @Query("id") playlistId: String,
        @Query("part") part: String = "snippet",
    ): PlaylistDetailResponse

    @GET("playlistItems")
    suspend fun getPlaylistVideoIds(
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "contentDetails",
        @Query("maxResults") maxResults: Int = 50,
        @Query("fields") fields: String = "items/contentDetails/videoId, nextPageToken",
        @Query("pageToken") pageToken: String? = "",
    ): PlaylistVideoIdResponse

    @GET("videos")
    suspend fun getPlaylistVideoDetails(
        @Query("id") videoId: String,
        @Query("part") part: String = "contentDetails",
        @Query("fields") fields: String = "items/contentDetails/duration",
    ): PlaylistVideoDetailResponse

}