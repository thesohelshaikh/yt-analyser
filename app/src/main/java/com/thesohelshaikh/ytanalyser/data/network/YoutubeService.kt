package com.thesohelshaikh.ytanalyser.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thesohelshaikh.ytanalyser.BuildConfig
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
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
private val contentType = "application/json".toMediaType()
private val jsonConfig = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}


private var networkInterceptor: Interceptor = Interceptor { chain ->
    var request: Request = chain.request()
    val url: HttpUrl = request.url.newBuilder()
        .addQueryParameter("key", BuildConfig.YOUTUBE_API_KEY)
        .build()
    request = request.newBuilder().url(url).build()
    chain.proceed(request)
}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    setLevel(HttpLoggingInterceptor.Level.BODY)
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(networkInterceptor)
    .build()

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

    companion object {
        fun create(): YoutubeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(jsonConfig.asConverterFactory(contentType))
                .build()
                .create(YoutubeService::class.java)
        }
    }
}