package com.thesohelshaikh.ytanalyser.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import com.thesohelshaikh.ytanalyser.data.model.ResourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    private val _historyScreenState = MutableLiveData<HistoryUiState>()
    val historyScreenState: LiveData<HistoryUiState> get() = _historyScreenState

    private fun VideoEntity.asHistoryItem(): HistoryItem = HistoryItem(
        id,
        snippet?.thumbnail,
        snippet?.title.toString(),
        snippet?.channelTitle.toString(),
        resourceType,
        createdAt,
    )

    private fun PlayListEntity.asHistoryItem(): HistoryItem = HistoryItem(
        id,
        thumbnailUrl,
        title.toString(),
        channelTitle.toString(),
        resourceType,
        createdAt,
    )

    fun getVideosAndPlaylists() {
        viewModelScope.launch {
            val videos = videoDao.getAll().map { it.asHistoryItem() }
            val playlists = playlistDao.getAll().map { it.asHistoryItem() }

            val historyItems = (videos + playlists).sortedByDescending { it.createdAt }

            _historyScreenState.value = HistoryUiState.Success(historyItems)
        }
    }

    fun getVideos() {
        viewModelScope.launch {
            val videos = videoDao.getAll().map { it.asHistoryItem() }

            val historyItems = (videos).sortedByDescending { it.createdAt }

            _historyScreenState.value = HistoryUiState.Success(historyItems)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            val playlists = playlistDao.getAll().map { it.asHistoryItem() }

            val historyItems = (playlists).sortedByDescending { it.createdAt }

            _historyScreenState.value = HistoryUiState.Success(historyItems)
        }
    }

    fun deleteItem(item: HistoryItem) {
        viewModelScope.launch {
            if (item.resourceType == ResourceType.VIDEO) {
                videoDao.delete(item.id)
            } else {
                playlistDao.delete(item.id)
            }
        }
    }

    sealed class HistoryUiState {
        class Success(val historyItems: List<HistoryItem>) : HistoryUiState()
    }

}