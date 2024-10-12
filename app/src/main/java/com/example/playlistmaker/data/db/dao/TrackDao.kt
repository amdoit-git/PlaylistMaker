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

    @Query("DELETE FROM tracks WHERE  trackId IN(SELECT t.trackId FROM tracks t LEFT JOIN tracks_in_favorite f ON t.trackId=f.trackId WHERE f.trackId IS NULL)")
    suspend fun deleteUnusedTrackInfo()

//    @Query("DELETE FROM tracks_in_playlists WHERE  trackId IN(SELECT t.trackId FROM tracks_in_playlists t LEFT JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.trackId IS NULL)")
//    suspend fun deleteUnusedTrackInPlaylist()

    @Query("SELECT sqlite_version()")
    suspend fun getDBVersion():String

    @Transaction
    suspend fun doOnConnect(){
        deleteUnusedTrackInfo()
        //deleteUnusedTrackInPlaylist()
    }
}