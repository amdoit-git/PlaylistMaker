package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.models.RoomTrack

@Dao
interface TrackDao {

    fun timestamp(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInfo(track: RoomTrack)

    @Query("SELECT * FROM tracks WHERE trackId=:trackId")
    suspend fun getTrackInfo(trackId: Int): RoomTrack?

    @Query("DELETE FROM tracks WHERE trackId NOT IN(SELECT DISTINCT trackId FROM playlist_track_map)")
    suspend fun doOnAppStart()
}