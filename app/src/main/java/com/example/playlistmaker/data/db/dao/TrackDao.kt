package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.RoomTrack

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInfo(track: RoomTrack)

    @Query("DELETE FROM tracks WHERE trackId NOT IN(SELECT trackId FROM tracks_in_favorite UNION SELECT trackId FROM tracks_in_history)")
    suspend fun deleteUnusedTrackInfo()

    @Query("DELETE FROM tracks_in_playlists WHERE trackId NOT IN(SELECT DISTINCT trackId FROM playlist_track_map)")
    suspend fun deleteUnusedTrackInPlaylist()

    @Query("SELECT sqlite_version()")
    suspend fun getDBVersion():String

    @Transaction
    suspend fun doOnConnect(){
        deleteUnusedTrackInfo()
        deleteUnusedTrackInPlaylist()
    }
}