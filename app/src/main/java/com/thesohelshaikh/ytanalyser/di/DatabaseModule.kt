package com.thesohelshaikh.ytanalyser.di

import android.content.Context
import androidx.room.Room
import com.thesohelshaikh.ytanalyser.data.local.YTDatabase
import com.thesohelshaikh.ytanalyser.data.local.dao.PlaylistDao
import com.thesohelshaikh.ytanalyser.data.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): YTDatabase {
        return Room.databaseBuilder(
            app,
            YTDatabase::class.java,
            "yt_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(ytDatabase: YTDatabase): VideoDao {
        return ytDatabase.videoDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(ytDatabase: YTDatabase): PlaylistDao {
        return ytDatabase.playlistDao()
    }
}