package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    fun clearCache() {
        viewModelScope.launch {
            videoDao.deleteAll()
            playlistDao.deleteAll()
        }
    }
}