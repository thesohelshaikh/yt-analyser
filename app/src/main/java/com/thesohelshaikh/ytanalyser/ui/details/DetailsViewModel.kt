package com.thesohelshaikh.ytanalyser.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity
import com.thesohelshaikh.ytanalyser.data.network.YoutubeNetworkService
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.asEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
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
        Timber.d("getVideoDetails: $id")
        _detailsScreenState.value = DetailsScreenState.LoadingState

        viewModelScope.launch {
            try {

                val localVideo = videoDao.get(id)

                if (localVideo != null) {
                    val snippet = localVideo.snippet
                    val contentDetails = localVideo.contentDetails
                    val thumbnail = snippet?.thumbnail
                    val durations = DurationsManger.parseTime(contentDetails?.duration)

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
                    val durations = DurationsManger.parseTime(contentDetails?.duration)

                    video?.asEntity()?.let { videoDao.upsert(it) }

                    _detailsScreenState.value = DetailsScreenState.SuccessState(
                        thumbnailUrl = thumbnail,
                        title = snippet?.title,
                        channelTitle = snippet?.channelTitle,
                        duration = durations.first()
                    )
                }

            } catch (e: Exception) {
                Timber.e("getVideoDetails: $e")
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
                Timber.e(e)
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
                Timber.i("getPlaylistVideoIds: videoId:$videoIds")

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
        val total = DurationsManger.parsePlaylistDurations(durations)


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

}