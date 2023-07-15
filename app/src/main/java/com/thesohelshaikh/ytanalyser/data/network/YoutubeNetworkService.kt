package com.thesohelshaikh.ytanalyser.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoDetailResponse
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.VideoDetailResponse
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

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

class YoutubeNetworkService : YoutubeService {

    private val youtubeService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(jsonConfig.asConverterFactory(contentType))
        .build()
        .create(YoutubeService::class.java)

    override suspend fun getVideoDetails(videoId: String, part: String): VideoDetailResponse {
        return youtubeService.getVideoDetails(videoId)
    }

    override suspend fun getPlaylistDetails(
        playlistId: String,
        part: String
    ): PlaylistDetailResponse {
        return youtubeService.getPlaylistDetails(playlistId)
    }

    override suspend fun getPlaylistVideoIds(
        playlistId: String,
        part: String,
        maxResults: Int,
        fields: String,
        pageToken: String?
    ): PlaylistVideoIdResponse {
        return youtubeService.getPlaylistVideoIds(playlistId = playlistId, pageToken = pageToken)
    }

    override suspend fun getPlaylistVideoDetails(
        videoId: String,
        part: String,
        fields: String
    ): PlaylistVideoDetailResponse {
        return youtubeService.getPlaylistVideoDetails(videoId)
    }
}