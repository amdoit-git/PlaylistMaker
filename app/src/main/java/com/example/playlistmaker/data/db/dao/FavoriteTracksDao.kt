package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.RoomTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao : TrackDao {

    @Transaction
    suspend fun saveTrack(track: RoomTrack) {
        insertTrackInfo(track)
        insertTrack(trackId = track.trackId, addedDate = (System.currentTimeMillis()/1000).toInt())
    }

    @Query("UPDATE tracks_in_favorite SET addedDate=unixepoch() WHERE trackId=:trackId")
    suspend fun updateTrackDate(trackId: Int)

    @Query("REPLACE INTO tracks_in_favorite (trackId, addedDate) VALUES (:trackId, :addedDate)")
    suspend fun insertTrack(trackId: Int, addedDate: Int)

    @Query("DELETE FROM tracks_in_favorite WHERE trackId=:trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_favorite x ON t.trackId=x.trackId WHERE x.trackId IN(:trackId) ORDER BY x.addedDate DESC")
    suspend fun findTracksById(vararg trackId: Int): List<RoomTrack>

    @Query("SELECT trackId FROM tracks_in_favorite WHERE trackId IN(:trackId)")
    suspend fun containsTracks(vararg trackId: Int): List<Int>

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_favorite x ON t.trackId=x.trackId ORDER BY x.addedDate DESC")
    fun getAllTracks(): Flow<List<RoomTrack>>

    @Query("SELECT trackId FROM tracks_in_favorite")
    fun getAllTracksIds(): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM tracks_in_favorite")
    suspend fun countTracks(): Int

    @Query("DELETE FROM tracks_in_favorite")
    suspend fun clearTracks()
}