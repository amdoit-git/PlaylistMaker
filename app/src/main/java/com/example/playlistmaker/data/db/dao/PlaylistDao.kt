package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.models.PlaylistTrackMap
import com.example.playlistmaker.data.db.models.RoomPlaylist
import com.example.playlistmaker.data.db.models.RoomTrackPlaylist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao : TrackDao {

    @Transaction
    suspend fun addPlaylist(playlist: RoomPlaylist): Int {
        val playlistId = insertPlaylist(playlist).toInt()
        return playlistId
    }

    @Insert
    suspend fun insertPlaylist(playlist: RoomPlaylist): Long

    @Query("UPDATE playlists SET coverFileName=:cover WHERE playlistId=:playlistId")
    suspend fun renameCover(playlistId: Int, cover: String)

    @Transaction
    suspend fun addTrack(track: RoomTrackPlaylist, playlistId: Int): Int {
        val addedDate = timestamp()
        saveTrackInfo(track)
        addToMap(
            PlaylistTrackMap(
                trackId = track.trackId,
                playlistId = playlistId,
                addedDate = addedDate
            )
        )
        moveToTop(playlistId, addedDate)
        return calcTracksTotal(playlistId)
    }

    @Transaction
    suspend fun deleteTrack(trackId: Int, playlistId: Int): Int {
        removeFromMap(trackId, playlistId)
        return calcTracksTotal(playlistId)
    }

    @Transaction
    suspend fun calcTracksTotal(playlistId: Int): Int {
        val tracksTotal = countTracks(playlistId)
        updatePlaylistLength(playlistId, tracksTotal)
        return tracksTotal
    }

    @Query("UPDATE playlists SET lastMod=:lastMod WHERE playlistId=:playlistId")
    suspend fun moveToTop(playlistId: Int, lastMod: Int)

    @Query("UPDATE playlists SET tracksTotal=:tracksTotal WHERE playlistId=:playlistId")
    suspend fun updatePlaylistLength(playlistId: Int, tracksTotal: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTrackInfo(track: RoomTrackPlaylist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToMap(index: PlaylistTrackMap)

    @Query("DELETE FROM playlist_track_map WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun removeFromMap(trackId: Int, playlistId: Int)

    @Query("SELECT COUNT(*) FROM playlist_track_map WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun containsTrack(playlistId: Int, trackId: Int): Int

    @Query("SELECT t.* FROM tracks_in_playlists t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.playlistId=:playlistId ORDER BY m.addedDate DESC")
    fun getTracks(playlistId: Int): Flow<List<RoomTrackPlaylist>>

    @Query("SELECT * FROM playlists ORDER BY lastMod DESC")
    fun getPlaylists(): Flow<List<RoomPlaylist>>

    @Query("SELECT COUNT(*) FROM playlist_track_map WHERE playlistId=:playlistId")
    suspend fun countTracks(playlistId: Int): Int
}