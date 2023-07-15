package com.thesohelshaikh.ytanalyser.data.local.dao

import androidx.room.*
import com.thesohelshaikh.ytanalyser.data.local.entities.PlayListEntity

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    suspend fun getAll(): List<PlayListEntity>

    @Query("SELECT * FROM playlists WHERE id=:id")
    suspend fun get(id: String): PlayListEntity?

    @Upsert
    suspend fun upsert(playlist: PlayListEntity)

    @Delete
    suspend fun delete(playlist: PlayListEntity)

    @Query("DELETE FROM playlists")
    suspend fun deleteAll()

}