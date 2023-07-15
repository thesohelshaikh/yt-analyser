package com.thesohelshaikh.ytanalyser.data.local.dao

import androidx.room.*
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    suspend fun getAll(): List<VideoEntity>

    @Query("SELECT * FROM videos WHERE id=:id")
    suspend fun get(id: String): VideoEntity?

    @Upsert
    suspend fun upsert(video: VideoEntity)

    @Delete
    suspend fun delete(video: VideoEntity)

    @Query("DELETE FROM videos")
    suspend fun deleteAll()

}