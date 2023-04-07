package com.thesohelshaikh.ytanalyser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity

@Database(
        entities = [VideoEntity::class],
        version = 1,
        exportSchema = true
)
@TypeConverters(RoomTypeConverters::class)
abstract class YTDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: YTDatabase? = null

        fun getDatabase(context: Context): YTDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        YTDatabase::class.java,
                        "yt_database"
                ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}