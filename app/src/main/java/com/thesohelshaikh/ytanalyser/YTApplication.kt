package com.thesohelshaikh.ytanalyser

import android.app.Application
import com.thesohelshaikh.ytanalyser.logging.CrashReporter
import com.thesohelshaikh.ytanalyser.logging.CrashlyticsLoggingTree
import com.thesohelshaikh.ytanalyser.logging.YTLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class YTApplication : Application() {

    @Inject
    lateinit var crashReporter: CrashReporter

    override fun onCreate() {
        super.onCreate()
        setupLogging()

        Timber.i("Application created")
    }

    private fun setupLogging() {
        Timber.plant(YTLoggingTree())
        Timber.plant(CrashlyticsLoggingTree(crashReporter))
    }
}