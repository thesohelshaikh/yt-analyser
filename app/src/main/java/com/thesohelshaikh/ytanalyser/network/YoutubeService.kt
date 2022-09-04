package com.thesohelshaikh.ytanalyser.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thesohelshaikh.ytanalyser.model.VideoDetailResponse
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
}