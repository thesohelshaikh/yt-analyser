package com.thesohelshaikh.ytanalyser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity

@Database(
    entities = [VideoEntity::class, PlayListEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class YTDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    abstract fun playlistDao(): PlaylistDao
}