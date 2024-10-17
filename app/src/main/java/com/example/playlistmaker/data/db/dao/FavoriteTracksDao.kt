package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.playlistmaker.data.db.models.RoomTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao : PlaylistDao {

    suspend fun saveTrack(track: RoomTrack) {
        addTrack(track, PLAYLIST_ID)
    }

    suspend fun deleteTrack(trackId: Int) {
        deleteTrack(trackId, PLAYLIST_ID)
    }

    suspend fun getAllTracks(): Flow<List<RoomTrack>> {
        return getTracks(PLAYLIST_ID)
    }

    @Query("SELECT COUNT(*) FROM playlist_track_map WHERE playlistId=$PLAYLIST_ID")
    suspend fun countTracks(): Int

    @Query("DELETE FROM playlist_track_map WHERE playlistId=$PLAYLIST_ID")
    suspend fun clearTracks()

    suspend fun containsTracks(trackId: Int): Int {
        return containsTrack(PLAYLIST_ID, trackId)
    }

    companion object {
        private const val PLAYLIST_ID = -2
    }
}