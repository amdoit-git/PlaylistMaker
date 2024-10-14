package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.RoomTrack

@Dao
interface HistoryTracksDao : TrackDao {

    @Transaction
    suspend fun saveTrack(track: RoomTrack) {
        insertTrackInfo(track)
        insertTrack(trackId = track.trackId)
        deleteTracksOverLimit()
    }

    @Query("UPDATE tracks_in_history SET addedDate=unixepoch() WHERE trackId=:trackId")
    suspend fun updateTrackDate(trackId: Int)

    @Query("REPLACE INTO tracks_in_history (trackId, addedDate) VALUES (:trackId, unixepoch())")
    suspend fun insertTrack(trackId: Int)

    @Query("DELETE FROM tracks_in_history WHERE trackId NOT IN(SELECT trackId FROM tracks_in_history ORDER BY addedDate DESC LIMIT $MAX_TRACKS_IN_LIST)")
    suspend fun deleteTracksOverLimit()

    @Query("DELETE FROM tracks_in_history WHERE trackId=:trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_history x WHERE x.trackId IN(:trackId) ORDER BY x.addedDate DESC")
    suspend fun findTracksById(vararg trackId: Int): List<RoomTrack>

    @Query("SELECT trackId FROM tracks_in_history WHERE trackId IN(:trackId)")
    suspend fun containsTracks(vararg trackId: Int): List<Int>

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_history x ORDER BY x.addedDate DESC")
    suspend fun getAllTracks(): List<RoomTrack>

    @Query("SELECT trackId FROM tracks_in_history")
    suspend fun getAllTracksIds(): List<Int>

    @Query("SELECT COUNT(*) FROM tracks_in_history")
    suspend fun countTracks(): Int

    @Query("DELETE FROM tracks_in_history")
    suspend fun clearTracks()

    companion object {
        private const val MAX_TRACKS_IN_LIST: Int = 10
    }
}