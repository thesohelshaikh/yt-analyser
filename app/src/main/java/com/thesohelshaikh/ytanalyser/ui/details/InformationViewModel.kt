package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesohelshaikh.ytanalyser.model.VideoDetailResponse
import com.thesohelshaikh.ytanalyser.network.YoutubeNetwork
import kotlinx.coroutines.launch

class InformationViewModel : ViewModel() {

    private val youtubeNetwork = YoutubeNetwork()

    val videoResponse = MutableLiveData<VideoDetailResponse>()

    fun getVideoDetails(id: String) {
        viewModelScope.launch {
            try {
                val response = youtubeNetwork.getVideoDetails(id)
                videoResponse.value = response
            } catch (e: Exception) {
                Log.e("TAG", "getVideoDetails: $e")
            }
        }
    }
}