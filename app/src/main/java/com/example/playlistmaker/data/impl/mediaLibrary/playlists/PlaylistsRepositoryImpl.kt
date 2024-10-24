package com.example.playlistmaker.data.impl.mediaLibrary.playlists

import android.graphics.Bitmap
import android.net.Uri
import com.example.playlistmaker.data.db.converters.PlaylistToRoomPlaylistMapper
import com.example.playlistmaker.data.db.converters.TrackToRoomTrackMapper
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.mediaLibrary.playlists.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File

class PlaylistsRepositoryImpl(private val saver: ImageSaver, private val dao: PlaylistDao) :
    PlaylistsRepository {

    private fun getCoverFilename(isCoverExists: Boolean): String {
        return if (isCoverExists) "${System.currentTimeMillis()}.jpg" else ""
    }

    override suspend fun <T> saveCoverToTmpDir(cover: T): Uri {

        return when (cover) {
            is Bitmap -> return saver.saveToTmpStorage(cover)
            is Uri -> saver.saveToTmpStorage(cover)
            is File -> saver.saveToTmpStorage(cover)
            else -> throw IllegalArgumentException("Неизвестный тип Playlist Cover")
        }
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {

        val coverFileName = getCoverFilename(
            isCoverExists = (playlist.coverUri != null)
        )

        playlist.coverUri?.let { uri ->
            saver.moveCoverFile(uri, coverFileName)
        }

        dao.addPlaylist(
            PlaylistToRoomPlaylistMapper.map(playlist, coverFileName)
        )
    }

    override suspend fun addTrack(track: Track, playlistId: Int) {

        dao.addTrack(
            TrackToRoomTrackMapper.map(track), playlistId
        )
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return dao.getPlaylists().flowOn(Dispatchers.IO)
            .map { PlaylistToRoomPlaylistMapper.map(it, saver) }
    }

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return dao.getTracks(playlistId).flowOn(Dispatchers.IO)
            .map { TrackToRoomTrackMapper.map(it) }
    }

    override suspend fun containsTrack(playlistId: Int, trackId: Int): Boolean {
        return dao.containsTrack(playlistId, trackId) > 0
    }
}