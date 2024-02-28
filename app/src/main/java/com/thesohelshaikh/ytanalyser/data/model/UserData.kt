package com.thesohelshaikh.ytanalyser.data.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
) {
    /**
     * Returns `true` if dark theme should be used.
     */
    @Composable
    fun shouldUseDarkTheme(
    ): Boolean = when (darkThemeConfig) {
        DarkThemeConfig.FOLLOWS_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.DARK -> true
        DarkThemeConfig.LIGHT -> false
    }
}
