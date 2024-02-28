package com.thesohelshaikh.ytanalyser.data.model

import androidx.annotation.StringRes
import com.thesohelshaikh.ytanalyser.R

enum class DarkThemeConfig(@StringRes val displayValue: Int) {
    FOLLOWS_SYSTEM(R.string.label_theme_system),
    LIGHT(R.string.label_theme_light),
    DARK(R.string.label_theme_dark);
}