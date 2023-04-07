package com.thesohelshaikh.ytanalyser.data.local

import androidx.room.*
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    fun getAll(): List<VideoEntity>

    @Query("SELECT * FROM videos WHERE id=:id")
    suspend fun get(id : String): VideoEntity?

    @Upsert
    suspend fun upsert(video: VideoEntity)

    @Delete
    suspend fun delete(video: VideoEntity)

}