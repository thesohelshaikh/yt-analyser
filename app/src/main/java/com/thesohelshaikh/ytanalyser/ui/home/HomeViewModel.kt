package com.thesohelshaikh.ytanalyser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.PreferenceDataSource
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.asHistoryItem
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferenceDataSource: PreferenceDataSource,
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    val useClipboard = preferenceDataSource.data.map {
        it.useClipboard
    }

    val historyItemsFlow = MutableStateFlow<List<HistoryItem>>(emptyList())

    fun getVideosAndPlaylists() {
        viewModelScope.launch {
            val videos = videoDao.getAll().map { it.asHistoryItem() }
            val playlists = playlistDao.getAll().map { it.asHistoryItem() }

            val historyItems = (videos + playlists)
                .sortedByDescending { it.createdAt }
                .take(5)

            historyItemsFlow.value = historyItems
        }
    }
}