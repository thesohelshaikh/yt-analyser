package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val showDialog = remember { mutableStateOf(false) }

    SettingsContent(showDialog)

    if (showDialog.value) {
        ClearCacheConfirmationDialog(showDialog) {
            settingsViewModel.clearCache()
        }
    }
}

