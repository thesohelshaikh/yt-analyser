package com.thesohelshaikh.ytanalyser.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.thesohelshaikh.ytanalyser.YTApplication
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val videoDao: VideoDao
) : ViewModel() {

    private val _historyScreenState = MutableLiveData<HistoryUiState>()
    val historyScreenState: LiveData<HistoryUiState> get() = _historyScreenState

    fun getVideos() {
        viewModelScope.launch {
            val videos = videoDao.getAll()
            _historyScreenState.value = HistoryUiState.Success(videos.reversed())
        }
    }

    sealed class HistoryUiState {
        class Success(val videos: List<VideoEntity>) : HistoryUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val applicationKey = ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
                val videoDao = (this[applicationKey] as YTApplication).videoDao
                HistoryViewModel(videoDao)
            }
        }
    }
}