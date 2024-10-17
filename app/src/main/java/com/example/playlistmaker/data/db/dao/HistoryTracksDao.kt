package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.HistoryMap
import com.example.playlistmaker.data.db.models.RoomTrack

@Dao
interface HistoryTracksDao : TrackDao {

    @Transaction
    suspend fun saveTrack(track: RoomTrack) {
        insertTrackInfo(track)
        insertTrack(HistoryMap(
            trackId = track.trackId,
            addedDate = timestamp()
        ))
        deleteTracksOverLimit()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: HistoryMap)

    @Query("DELETE FROM tracks_in_history WHERE trackId NOT IN(SELECT trackId FROM tracks_in_history ORDER BY addedDate DESC LIMIT $MAX_TRACKS_IN_LIST)")
    suspend fun deleteTracksOverLimit()

    @Query("DELETE FROM tracks_in_history WHERE trackId=:trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_history h ON t.trackId=h.trackId WHERE h.trackId IN(:trackId) ORDER BY h.addedDate DESC")
    suspend fun findTracksById(vararg trackId: Int): List<RoomTrack>

    @Query("SELECT trackId FROM tracks_in_history WHERE trackId IN(:trackId)")
    suspend fun containsTracks(vararg trackId: Int): List<Int>

    @Query("SELECT t.* FROM tracks t INNER JOIN tracks_in_history h ON t.trackId=h.trackId ORDER BY h.addedDate DESC")
    suspend fun getAllTracks(): List<RoomTrack>

    @Query("SELECT COUNT(*) FROM tracks_in_history")
    suspend fun countTracks(): Int

    @Query("DELETE FROM tracks_in_history")
    suspend fun clearTracks()

    companion object {
        private const val MAX_TRACKS_IN_LIST: Int = 10
    }
}