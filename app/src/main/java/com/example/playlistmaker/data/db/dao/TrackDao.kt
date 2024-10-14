package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.TrackInDB

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInfo(track: TrackInDB)

    @Query("DELETE FROM tracks WHERE  trackId NOT IN(SELECT trackId FROM tracks_in_favorite)")
    suspend fun deleteUnusedTrackInfo()

    @Query("SELECT sqlite_version()")
    suspend fun getDBVersion():String

    @Transaction
    suspend fun doOnConnect(){
        deleteUnusedTrackInfo()
    }
}