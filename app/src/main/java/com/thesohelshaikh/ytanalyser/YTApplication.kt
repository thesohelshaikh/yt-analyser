package com.thesohelshaikh.ytanalyser

import android.app.Application
import com.thesohelshaikh.ytanalyser.data.local.YTDatabase
import com.thesohelshaikh.ytanalyser.logging.SentryLoggingTree
import com.thesohelshaikh.ytanalyser.logging.YTLoggingTree
import timber.log.Timber


class YTApplication : Application() {

    val database by lazy { YTDatabase.getDatabase(this) }
    val videoDao by lazy { database.videoDao() }
    val playlistDao by lazy { database.playlistDao() }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(YTLoggingTree())
        } else {
            Timber.plant(SentryLoggingTree())
        }
    }
}