package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.network.YoutubeNetwork
import kotlinx.coroutines.launch

class InformationViewModel : ViewModel() {

    private val youtubeNetwork = YoutubeNetwork()

    private val _detailsScreenState = MutableLiveData<DetailsScreenState>()
    val detailsScreenState: LiveData<DetailsScreenState> get() = _detailsScreenState

    sealed class DetailsScreenState {
        class SuccessState(
            val thumbnailUrl: String?,
            val title: String?,
            val channelTitle: String?,
            val duration: Long
        ) : DetailsScreenState()

        class ErrorState(val message: String) : DetailsScreenState()
        object LoadingState : DetailsScreenState()
    }

    fun getVideoDetails(id: String) {
        _detailsScreenState.value = DetailsScreenState.LoadingState

        viewModelScope.launch {
            try {
                val response = youtubeNetwork.getVideoDetails(id)
                val snippet = response.items?.get(0)?.snippet
                val contentDetails = response.items?.get(0)?.contentDetails
                val thumbnail = snippet?.thumbnails?.getThumbnailUrl()
                val durations = UtilitiesManger.parseTime(contentDetails?.duration)
                _detailsScreenState.value = DetailsScreenState.SuccessState(
                    thumbnailUrl = thumbnail,
                    title = snippet?.title,
                    channelTitle = snippet?.channelTitle,
                    duration = durations.first()
                )
            } catch (e: Exception) {
                Log.e("TAG", "getVideoDetails: $e")
                _detailsScreenState.value = DetailsScreenState.ErrorState(e.message.toString())
            }
        }
    }

    fun getPlaylistVideoIds(playlistId: String) {
        _detailsScreenState.value = DetailsScreenState.LoadingState

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

                _detailsScreenState.value = DetailsScreenState.SuccessState(
                    thumbnailUrl = thumbnail,
                    title = snippet?.title,
                    channelTitle = snippet?.channelTitle,
                    duration = total
                )
            } catch (e: Exception) {
                Log.e("TAG", "Error", e)
                _detailsScreenState.value = DetailsScreenState.ErrorState(e.message.toString())
            }
        }
    }
}