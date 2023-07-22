package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thesohelshaikh.ytanalyser.YTApplication
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity
import com.thesohelshaikh.ytanalyser.data.network.YoutubeNetworkService
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.asEntity
import kotlinx.coroutines.launch

class InformationViewModel(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    private val youtubeNetworkService = YoutubeNetworkService()

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
        Log.d("TAG", "getVideoDetails: ")
        _detailsScreenState.value = DetailsScreenState.LoadingState

        viewModelScope.launch {
            try {

                val localVideo = videoDao.get(id)

                if (localVideo != null) {
                    val snippet = localVideo.snippet
                    val contentDetails = localVideo.contentDetails
                    val thumbnail = snippet?.thumbnail
                    val durations = UtilitiesManger.parseTime(contentDetails?.duration)

                    _detailsScreenState.value = DetailsScreenState.SuccessState(
                        thumbnailUrl = thumbnail,
                        title = snippet?.title,
                        channelTitle = snippet?.channelTitle,
                        duration = durations.first()
                    )
                } else {
                    val response = youtubeNetworkService.getVideoDetails(id)

                    val video = response.items?.get(0)
                    val snippet = video?.snippet
                    val contentDetails = video?.contentDetails
                    val thumbnail = snippet?.thumbnails?.getThumbnailUrl()
                    val durations = UtilitiesManger.parseTime(contentDetails?.duration)

                    video?.asEntity()?.let { videoDao.upsert(it) }

                    _detailsScreenState.value = DetailsScreenState.SuccessState(
                        thumbnailUrl = thumbnail,
                        title = snippet?.title,
                        channelTitle = snippet?.channelTitle,
                        duration = durations.first()
                    )
                }

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

                val localPlaylist = playlistDao.get(playlistId)

                if (localPlaylist != null) {
                    _detailsScreenState.value = DetailsScreenState.SuccessState(
                        thumbnailUrl = localPlaylist.thumbnailUrl,
                        title = localPlaylist.title,
                        channelTitle = localPlaylist.channelTitle,
                        duration = localPlaylist.duration
                    )
                } else {
                    fetchPlaylistDetailsFromNetwork(playlistId)
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error", e)
                _detailsScreenState.value = DetailsScreenState.ErrorState(e.message.toString())
            }
        }
    }

    private suspend fun fetchPlaylistDetailsFromNetwork(playlistId: String) {
        var nextPageToken: String? = null
        val durations = ArrayList<String>()

        val playlistDetailResponse = youtubeNetworkService.getPlaylistDetails(playlistId)

        while (true) {
            val items = ArrayList<PlaylistVideoIdResponse.Item?>()

            val response =
                youtubeNetworkService.getPlaylistVideoIds(playlistId, pageToken = nextPageToken)

            if (!response.items.isNullOrEmpty()) {
                items.addAll(response.items)
                val videoIds = items.joinToString(separator = ",") {
                    it?.contentDetails?.videoId ?: ""
                }
                Log.i("TAG", "getPlaylistVideoIds: videoId:$videoIds")

                // get durations of each of the videos
                val videosResponse = youtubeNetworkService.getPlaylistVideoDetails(videoIds)
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

        playlistDao.upsert(
            PlayListEntity(
                playlistId,
                thumbnail,
                snippet?.title,
                snippet?.channelTitle,
                total
            )
        )

        _detailsScreenState.value = DetailsScreenState.SuccessState(
            thumbnailUrl = thumbnail,
            title = snippet?.title,
            channelTitle = snippet?.channelTitle,
            duration = total
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val ytApplication =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YTApplication
                val videoDao = ytApplication.videoDao
                val playlistDao = ytApplication.playlistDao
                InformationViewModel(videoDao, playlistDao)
            }
        }
    }
}