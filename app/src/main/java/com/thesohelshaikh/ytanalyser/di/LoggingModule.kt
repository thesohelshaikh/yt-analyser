package com.thesohelshaikh.ytanalyser.di

import com.thesohelshaikh.ytanalyser.logging.CrashReporter
import com.thesohelshaikh.ytanalyser.logging.FirebaseCrashReporter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Singleton
    @Provides
    fun provideCrashReporter(): CrashReporter {
        return FirebaseCrashReporter.init()
    }
}