package com.thesohelshaikh.ytanalyser.di

import com.thesohelshaikh.ytanalyser.data.network.YoutubeNetworkRepository
import com.thesohelshaikh.ytanalyser.data.network.YoutubeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideYoutubeService(): YoutubeService {
        return YoutubeService.create()
    }

    @Singleton
    @Provides
    fun provideYoutubeNetworkRepository(
        youtubeService: YoutubeService
    ): YoutubeNetworkRepository {
        return YoutubeNetworkRepository(youtubeService)
    }
}