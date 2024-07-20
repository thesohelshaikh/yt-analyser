package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import kotlinx.coroutines.flow.first

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val showDialog = remember { mutableStateOf(false) }
    val currentTheme = remember { mutableStateOf(DarkThemeConfig.FOLLOWS_SYSTEM) }
    val shouldUseClipboard = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingsViewModel.preferences.first().run {
            currentTheme.value = darkThemeConfig
            shouldUseClipboard.value = useClipboard
        }
    }

    SettingsContent(
        showDialog = showDialog,
        currentTheme = currentTheme.value,
        shouldUseClipboard = shouldUseClipboard.value,
        onThemeUpdate = {
            settingsViewModel.setAppTheme(it)
            currentTheme.value = it
        },
        onUseClipboardToggle = {
            settingsViewModel.toggleUseClipboard()
            shouldUseClipboard.value = !shouldUseClipboard.value
        },
    )

    if (showDialog.value) {
        ClearCacheConfirmationDialog(showDialog) {
            settingsViewModel.clearCache()
        }
    }
}

