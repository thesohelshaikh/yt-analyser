package com.thesohelshaikh.ytanalyser

import android.app.Application
import com.thesohelshaikh.ytanalyser.logging.SentryLoggingTree
import com.thesohelshaikh.ytanalyser.logging.YTLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class YTApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(YTLoggingTree())
        } else {
            Timber.plant(SentryLoggingTree())
        }
    }
}