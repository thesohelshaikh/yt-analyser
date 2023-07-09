package com.thesohelshaikh.ytanalyser

import android.app.Application
import com.thesohelshaikh.ytanalyser.data.local.YTDatabase

class YTApplication: Application() {

    val database by lazy { YTDatabase.getDatabase(this) }
    val videoDao by lazy { database.videoDao() }
    val playlistDao by lazy { database.playlistDao() }
}