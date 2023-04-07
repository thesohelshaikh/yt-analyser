package com.thesohelshaikh.ytanalyser.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.VideoDetailResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
private val contentType = "application/json".toMediaType()
private val jsonConfig = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

const val API_KEY = "AIzaSyBJ-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk"

var networkInterceptor: Interceptor = Interceptor { chain ->
    var request: Request = chain.request()
    val url: HttpUrl = request.url.newBuilder().addQueryParameter("key", API_KEY).build()
    request = request.newBuilder().url(url).build()
    chain.proceed(request)
}

val loggingInterceptor = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BODY)
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(networkInterceptor)
    .build()

interface YoutubeApi {
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") videoId: String,
        @Query("part") part: String = "contentDetails, snippet, statistics",
    ): VideoDetailResponse

    @GET("playlistItems")
    suspend fun getPlaylistDetails(
        @Query("playlistId") playlistId: String,
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

class YoutubeNetwork : YoutubeApi {

    @OptIn(ExperimentalSerializationApi::class)
    private val youtubeApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(jsonConfig.asConverterFactory(contentType))
        .build()
        .create(YoutubeApi::class.java)

    override suspend fun getVideoDetails(videoId: String, part: String): VideoDetailResponse {
        return youtubeApi.getVideoDetails(videoId)
    }

    override suspend fun getPlaylistDetails(
        playlistId: String,
        part: String
    ): PlaylistDetailResponse {
        return youtubeApi.getPlaylistDetails(playlistId)
    }

    override suspend fun getPlaylistVideoIds(
        playlistId: String,
        part: String,
        maxResults: Int,
        fields: String,
        pageToken: String?
    ): PlaylistVideoIdResponse {
        return youtubeApi.getPlaylistVideoIds(playlistId = playlistId, pageToken = pageToken)
    }

    override suspend fun getPlaylistVideoDetails(
        videoId: String,
        part: String,
        fields: String
    ): PlaylistVideoDetailResponse {
        return youtubeApi.getPlaylistVideoDetails(videoId)
    }
}