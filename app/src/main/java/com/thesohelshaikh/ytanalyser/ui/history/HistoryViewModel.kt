package com.thesohelshaikh.ytanalyser.ui.history

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
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    private val _historyScreenState = MutableLiveData<HistoryUiState>()
    val historyScreenState: LiveData<HistoryUiState> get() = _historyScreenState

    data class HistoryItem(
        val id: String,
        val thumbnail: String?,
        val title: String,
        val channelTitle: String,
        val createdAt: Long,
    )

    private fun VideoEntity.asHistoryItem(): HistoryItem = HistoryItem(
        id,
        snippet?.thumbnail,
        snippet?.title.toString(),
        snippet?.channelTitle.toString(),
        createdAt,
    )

    private fun PlayListEntity.asHistoryItem(): HistoryItem = HistoryItem(
        id,
        thumbnailUrl,
        title.toString(),
        channelTitle.toString(),
        createdAt,
    )

    fun getVideos() {
        viewModelScope.launch {
            val videos = videoDao.getAll().map { it.asHistoryItem() }
            val playlists = playlistDao.getAll().map { it.asHistoryItem() }

            val historyItems = (videos + playlists).sortedByDescending { it.createdAt }

            _historyScreenState.value = HistoryUiState.Success(historyItems)
        }
    }

    sealed class HistoryUiState {
        class Success(val videos: List<HistoryItem>) : HistoryUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val applicationKey = ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                val videoDao = (this[applicationKey] as YTApplication).videoDao
                val playlistDao = (this[applicationKey] as YTApplication).playlistDao
                HistoryViewModel(videoDao, playlistDao)
            }
        }
    }
}