package com.thesohelshaikh.ytanalyser.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import com.thesohelshaikh.ytanalyser.data.model.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {

    private val appThemeKey = intPreferencesKey("app_theme")
    private val useClipboardKey = booleanPreferencesKey("use_clipboard")

    val data = userPreferences.data.map {
        UserData(
            darkThemeConfig = when (it[appThemeKey]) {
                1 -> DarkThemeConfig.LIGHT
                2 -> DarkThemeConfig.DARK
                else -> DarkThemeConfig.FOLLOWS_SYSTEM
            },
            useClipboard = it[useClipboardKey] ?: false
        )
    }

    suspend fun setAppTheme(darkThemeConfig: DarkThemeConfig) {
        userPreferences.edit {
            it[appThemeKey] = darkThemeConfig.ordinal
        }
    }

    suspend fun toggleUseClipboard() {
        val current = data.first().useClipboard
        userPreferences.edit {
            it[useClipboardKey] = !current
        }
    }
}