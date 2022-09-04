package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.model.VideoDetailResponse
import com.thesohelshaikh.ytanalyser.network.YoutubeNetwork
import kotlinx.coroutines.launch

class InformationViewModel : ViewModel() {

    private val youtubeNetwork = YoutubeNetwork()

    val videoResponse = MutableLiveData<VideoDetailResponse>()
    val playlistResponse = MutableLiveData<DetailsScreen>()

    data class DetailsScreen(
        val thumbnailUrl: String?,
        val title: String?,
        val channelTitle: String?,
        val duration: Long,
    )

    fun getVideoDetails(id: String) {
        viewModelScope.launch {
            try {
                val response = youtubeNetwork.getVideoDetails(id)
                videoResponse.value = response
            } catch (e: Exception) {
                Log.e("TAG", "getVideoDetails: $e")
            }
        }
    }

    fun getPlaylistVideoIds(playlistId: String) {
        viewModelScope.launch {
            try {
                val items = ArrayList<PlaylistVideoIdResponse.Item?>()
                var nextPageToken: String? = null
                val durations = ArrayList<String>()

                val playlistDetailResponse = youtubeNetwork.getPlaylistDetails(playlistId)

                while (true) {
                    val response =
                        youtubeNetwork.getPlaylistVideoIds(playlistId, pageToken = nextPageToken)

                    if (!response.items.isNullOrEmpty()) {
                        items.addAll(response.items)
                        val videoIds = response.items.joinToString(separator = ",") {
                            it?.contentDetails?.videoId ?: ""
                        }
                        Log.i("TAG", "getPlaylistVideoIds: videoId:$videoIds")

                        // get durations of each of the videos
                        val videosResponse = youtubeNetwork.getPlaylistVideoDetails(videoIds)
                        videosResponse.items?.forEach { video ->
                            video?.contentDetails?.duration?.let { durations.add(it) }
                        }
                    }

                    nextPageToken = response.nextPageToken
                    if (nextPageToken == null) {
                        break
                    }
                }
                val total = UtilitiesManger.parsePlaylistDurations(durations)


                val snippet = playlistDetailResponse.items?.first()?.snippet
                val thumbnail = snippet?.thumbnails?.getThumbnailUrl()

                playlistResponse.value = DetailsScreen(
                    thumbnailUrl = thumbnail,
                    title = snippet?.title,
                    channelTitle = snippet?.channelTitle,
                    duration = total
                )
            } catch (e: Exception) {
                Log.e("TAG", "Error", e)
            }
        }
    }
}