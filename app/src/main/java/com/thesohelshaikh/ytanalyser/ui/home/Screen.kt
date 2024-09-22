package com.thesohelshaikh.ytanalyser.ui.home

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data class Details(val videoId: String) : Screen()

    @Serializable
    data object History : Screen()

    @Serializable
    data object Settings : Screen()
}