package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.RoomTrack

@Dao
interface HistoryTracksDao : PlaylistDao {

    @Transaction
    suspend fun saveTrack(track: RoomTrack) {
        addTrack(track, PLAYLIST_ID)
        deleteTracksOverLimit(PLAYLIST_ID)
    }

    @Query("DELETE FROM playlist_track_map WHERE playlistId=:playlistId AND trackId NOT IN(SELECT trackId FROM playlist_track_map WHERE playlistId=:playlistId ORDER BY addedDate DESC LIMIT $MAX_TRACKS_IN_LIST)")
    suspend fun deleteTracksOverLimit(playlistId: Int)

    @Query("SELECT t.* FROM tracks t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE playlistId=$PLAYLIST_ID ORDER BY m.addedDate DESC")
    suspend fun getAllTracks(): List<RoomTrack>

    suspend fun clearTracks(){
        clearPlaylist(PLAYLIST_ID)
    }

    suspend fun containsTracks(trackId: Int): Int {
        return containsTrack(PLAYLIST_ID, trackId)
    }

    companion object {
        private const val MAX_TRACKS_IN_LIST: Int = 10
        private const val PLAYLIST_ID = -1
    }
}