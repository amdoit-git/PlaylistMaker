package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.data.db.models.PlaylistTrackMap
import com.example.playlistmaker.data.db.models.PlaylistUpdates
import com.example.playlistmaker.data.db.models.RoomPlaylist
import com.example.playlistmaker.data.db.models.RoomTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao : TrackDao {

    @Insert
    suspend fun addPlaylist(playlist: RoomPlaylist): Long

    @Update(entity = RoomPlaylist::class)
    suspend fun updatePlaylist(playlist: PlaylistUpdates)

    @Transaction
    suspend fun calcPlaylistLengthAndDuration(playlistId: Int) {
        val length = getPlaylistLength(playlistId)
        val duration = getPlaylistDuration(playlistId)
        updatePlaylistLengthAndDuration(
            playlistId = playlistId,
            tracksTotal = length,
            duration = duration
        )
    }

    @Query("SELECT SUM(t.duration) FROM tracks t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.playlistId=:playlistId")
    suspend fun getPlaylistDuration(playlistId: Int): Int

    @Query("SELECT COUNT(*) FROM tracks t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.playlistId=:playlistId")
    suspend fun getPlaylistLength(playlistId: Int): Int

    @Query("UPDATE playlists SET lastMod=:lastMod WHERE playlistId=:playlistId")
    suspend fun movePlaylistToTop(playlistId: Int, lastMod: Int)

    @Query("UPDATE playlists SET tracksTotal=:tracksTotal, duration=:duration WHERE playlistId=:playlistId")
    suspend fun updatePlaylistLengthAndDuration(playlistId: Int, tracksTotal: Int, duration: Int)

    @Query("SELECT * FROM playlists ORDER BY lastMod DESC")
    fun getPlaylists(): Flow<List<RoomPlaylist>>

    @Query("SELECT * FROM playlists WHERE playlistId=:playlistId")
    fun getPlaylistInfo(playlistId: Int): Flow<RoomPlaylist>

    @Query("SELECT coverFileName FROM playlists WHERE playlistId=:playlistId")
    suspend fun getPlaylistCover(playlistId: Int): String

    @Query("DELETE FROM playlists WHERE playlistId=:playlistId")
    suspend fun deletePlaylistInfo(playlistId: Int)

    @Query("DELETE FROM playlist_track_map WHERE playlistId=:playlistId")
    suspend fun clearPlaylist(playlistId: Int)

    @Transaction
    suspend fun deletePlaylist(playlistId: Int){
        clearPlaylist(playlistId)
        deletePlaylistInfo(playlistId)
    }

    //tracks

    @Transaction
    suspend fun addTrack(track: RoomTrack, playlistId: Int) {
        val addedDate = timestamp()
        insertTrackInfo(track)
        addToMap(
            PlaylistTrackMap(
                trackId = track.trackId,
                playlistId = playlistId,
                addedDate = addedDate
            )
        )
        if (playlistId > 0) {
            calcPlaylistLengthAndDuration(playlistId)
            movePlaylistToTop(playlistId, addedDate)
        }
    }

    @Transaction
    suspend fun deleteTrack(trackId: Int, playlistId: Int) {
        removeFromMap(trackId, playlistId)
        if (playlistId > 0) {
            calcPlaylistLengthAndDuration(playlistId)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToMap(index: PlaylistTrackMap)

    @Query("DELETE FROM playlist_track_map WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun removeFromMap(trackId: Int, playlistId: Int)

    @Query("SELECT COUNT(*) FROM playlist_track_map WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun containsTrack(playlistId: Int, trackId: Int): Int

    @Query("SELECT t.* FROM tracks t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.playlistId=:playlistId ORDER BY m.addedDate DESC")
    fun getTracks(playlistId: Int): Flow<List<RoomTrack>>
}