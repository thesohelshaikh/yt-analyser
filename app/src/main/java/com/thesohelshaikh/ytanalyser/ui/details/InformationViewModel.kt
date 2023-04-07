package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.lifecycle.*
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import com.thesohelshaikh.ytanalyser.data.local.VideoDao
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.YoutubeNetwork
import com.thesohelshaikh.ytanalyser.data.network.model.asEntity
import kotlinx.coroutines.launch

class InformationViewModel(
        private val videoDao: VideoDao
) : ViewModel() {

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
                    val response = youtubeNetwork.getVideoDetails(id)

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

class InformationViewModelFactory(private val dao: VideoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InformationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InformationViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}