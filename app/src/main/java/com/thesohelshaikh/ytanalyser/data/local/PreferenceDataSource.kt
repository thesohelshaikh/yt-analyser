package com.thesohelshaikh.ytanalyser.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import com.thesohelshaikh.ytanalyser.data.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
) {

    private val appThemeKey = intPreferencesKey("app_theme")

    val data = userPreferences.data.map {
        UserData(
            darkThemeConfig = when (it[appThemeKey]) {
                1 -> DarkThemeConfig.LIGHT
                2 -> DarkThemeConfig.DARK
                else -> DarkThemeConfig.FOLLOWS_SYSTEM
            }
        )
    }

    suspend fun setAppTheme(darkThemeConfig: DarkThemeConfig) {
        userPreferences.edit {
            it[appThemeKey] = darkThemeConfig.ordinal
        }
    }
}