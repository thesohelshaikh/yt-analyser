package com.thesohelshaikh.ytanalyser.ui.home

import androidx.lifecycle.ViewModel
import com.thesohelshaikh.ytanalyser.data.local.PreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferenceDataSource: PreferenceDataSource
) : ViewModel() {

    val useClipboard = preferenceDataSource.data.map {
        it.useClipboard
    }
}