package com.thesohelshaikh.ytanalyser.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.thesohelshaikh.ytanalyser.data.local.entities.VideoEntity

@Dao
interface VideoDao {

    @Query("SELECT * FROM videos")
    suspend fun getAll(): List<VideoEntity>

    @Query("SELECT * FROM videos WHERE id=:id")
    suspend fun get(id: String): VideoEntity?

    @Upsert
    suspend fun upsert(video: VideoEntity)

    @Query("DELETE FROM videos WHERE id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM videos")
    suspend fun deleteAll()

}