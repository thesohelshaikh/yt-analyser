package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.data.local.PreferenceDataSource
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val playlistDao: PlaylistDao,
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {

    val preferences = preferenceDataSource.data

    fun clearCache() {
        viewModelScope.launch {
            videoDao.deleteAll()
            playlistDao.deleteAll()
        }
    }

    fun setAppTheme(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            preferenceDataSource.setAppTheme(darkThemeConfig)
        }
    }

    fun toggleUseClipboard() {
        viewModelScope.launch {
            preferenceDataSource.toggleUseClipboard()
        }
    }
}