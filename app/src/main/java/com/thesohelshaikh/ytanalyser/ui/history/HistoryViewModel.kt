package com.thesohelshaikh.ytanalyser.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.asHistoryItem
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import com.thesohelshaikh.ytanalyser.data.model.ResourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    private val _historyScreenState = MutableStateFlow<HistoryUiState>(HistoryUiState.Initial)
    val historyScreenState: StateFlow<HistoryUiState> get() = _historyScreenState

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
        object Initial : HistoryUiState()
        class Success(val historyItems: List<HistoryItem>) : HistoryUiState()
    }

}