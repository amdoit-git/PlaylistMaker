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
import com.example.playlistmaker.domain.models.Playlist
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
        updatePlaylistLength(
            playlistId = playlistId,
            tracksTotal = length
        )
    }



    @Query("SELECT COUNT(*) FROM tracks t INNER JOIN playlist_track_map m ON t.trackId=m.trackId WHERE m.playlistId=:playlistId")
    suspend fun getPlaylistLength(playlistId: Int): Int

    @Query("UPDATE playlists SET lastMod=:lastMod WHERE playlistId=:playlistId")
    suspend fun movePlaylistToTop(playlistId: Int, lastMod: Int)

    @Query("UPDATE playlists SET tracksTotal=:tracksTotal WHERE playlistId=:playlistId")
    suspend fun updatePlaylistLength(playlistId: Int, tracksTotal: Int)

    @Query("SELECT * FROM playlists ORDER BY lastMod DESC")
    fun getPlaylists(): Flow<List<RoomPlaylist>>

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