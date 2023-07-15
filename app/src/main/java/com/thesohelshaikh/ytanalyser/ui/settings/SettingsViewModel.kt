package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thesohelshaikh.ytanalyser.YTApplication
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
) : ViewModel() {

    fun clearCache() {
        viewModelScope.launch {
            videoDao.deleteAll()
            playlistDao.deleteAll()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val applicationKey = ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                val videoDao = (this[applicationKey] as YTApplication).videoDao
                val playlistDao = (this[applicationKey] as YTApplication).playlistDao
                SettingsViewModel(videoDao, playlistDao)
            }
        }
    }
}