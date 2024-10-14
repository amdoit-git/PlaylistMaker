package com.example.playlistmaker.data.impl.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.converters.PlaylistToRoomPlaylistMapper
import com.example.playlistmaker.data.db.converters.TrackToRoomTrackPlaylistMapper
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File

class PlaylistsRepositoryImpl(private val saver: ImageSaver, database: TracksDB) :
    PlaylistsRepository {

    private val dao = database.playlistDao()

    override suspend fun <T> saveCoverToTmpDir(cover: T): Uri {

        return when (cover) {
            is Bitmap -> return saver.saveToTmpStorage(cover)
            is Uri -> saver.saveToTmpStorage(cover)
            is File -> saver.saveToTmpStorage(cover)
            else -> throw IllegalArgumentException("Неизвестный тип Playlist Cover")
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {

        val coverFileName = dao.addPlaylist(
            PlaylistToRoomPlaylistMapper.map(playlist)
        )

        playlist.coverUri?.let { uri ->
            saver.saveCover(uri, coverFileName)
        }
    }

    override suspend fun addTrack(track: Track, playlistId: Int): Int {

        return dao.addTrack(
            TrackToRoomTrackPlaylistMapper.map(track), playlistId
        )
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return dao.getPlaylists().flowOn(Dispatchers.IO)
            .map { PlaylistToRoomPlaylistMapper.map(it, saver) }
    }

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return dao.getTracks(playlistId).flowOn(Dispatchers.IO)
            .map { TrackToRoomTrackPlaylistMapper.map(it) }
    }

    override suspend fun containsTrack(playlistId: Int, trackId: Int): Boolean {
        return dao.containsTrack(playlistId, trackId) > 0
    }
}