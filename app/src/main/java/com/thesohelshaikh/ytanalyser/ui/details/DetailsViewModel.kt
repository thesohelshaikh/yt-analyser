package com.thesohelshaikh.ytanalyser.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity
import com.thesohelshaikh.ytanalyser.data.network.model.PlaylistVideoIdResponse
import com.thesohelshaikh.ytanalyser.data.network.model.asEntity
import com.thesohelshaikh.ytanalyser.data.repository.YoutubeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
    private val youtubeNetworkRepository: YoutubeRepository
) : ViewModel() {

    private val _detailsScreenState =
        MutableStateFlow<DetailsScreenState>(DetailsScreenState.InitialState)
    val detailsScreenState: StateFlow<DetailsScreenState> get() = _detailsScreenState

    sealed class DetailsScreenState {
        object InitialState : DetailsScreenState()
        class SuccessState(
            val id: String,
            val thumbnailUrl: String?,
            val title: String?,
            val channelTitle: String?,
            val duration: Long
        ) : DetailsScreenState()

        object ErrorState : DetailsScreenState()
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
                        id = localVideo.id,
                        thumbnailUrl = thumbnail,
                        title = snippet?.title,
                        channelTitle = snippet?.channelTitle,
                        duration = durations.first()
                    )
                } else {
                    val response = youtubeNetworkRepository.getVideoDetails(id)

                    val video = response.items?.get(0)
                    val snippet = video?.snippet
                    val contentDetails = video?.contentDetails
                    val thumbnail = snippet?.thumbnails?.getThumbnailUrl()
                    val durations = DurationsManger.parseTime(contentDetails?.duration)

                    video?.asEntity()?.let { videoDao.upsert(it) }

                    _detailsScreenState.value = DetailsScreenState.SuccessState(
                        id = id,
                        thumbnailUrl = thumbnail,
                        title = snippet?.title,
                        channelTitle = snippet?.channelTitle,
                        duration = durations.first()
                    )
                }

            } catch (e: Exception) {
                Timber.e("getVideoDetails: $e")
                _detailsScreenState.value = DetailsScreenState.ErrorState
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
                        id = playlistId,
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
                _detailsScreenState.value = DetailsScreenState.ErrorState
            }
        }
    }

    private suspend fun fetchPlaylistDetailsFromNetwork(playlistId: String) {
        var nextPageToken: String? = null
        val durations = ArrayList<String>()

        val playlistDetailResponse = youtubeNetworkRepository.getPlaylistDetails(playlistId)

        while (true) {
            val items = ArrayList<PlaylistVideoIdResponse.Item?>()

            val response =
                youtubeNetworkRepository.getPlaylistVideoIds(playlistId, pageToken = nextPageToken)

            if (!response.items.isNullOrEmpty()) {
                items.addAll(response.items)
                val videoIds = items.joinToString(separator = ",") {
                    it?.contentDetails?.videoId ?: ""
                }
                Timber.i("getPlaylistVideoIds: videoId:$videoIds")

                // get durations of each of the videos
                val videosResponse = youtubeNetworkRepository.getPlaylistVideoDetails(videoIds)
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
            id = playlistId,
            thumbnailUrl = thumbnail,
            title = snippet?.title,
            channelTitle = snippet?.channelTitle,
            duration = total
        )
    }

    @Suppress("DEPRECATION")
    fun shareDetailsScreenshot(context: Context, bitmap: Bitmap) {
        try {
            Timber.i("Captured bitmap: $bitmap")
            val bitmapPath = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "Screenshot ${Date()}",
                null
            )
            val bitmapUri = Uri.parse(bitmapPath)
            shareImageUri(context, bitmapUri)
        } catch (error: Throwable) {
            // Error occurred, do something.
            Timber.e(error)
        }
    }

    private fun shareImageUri(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setType("image/png")
        context.startActivity(intent)
    }

}