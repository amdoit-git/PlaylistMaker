package com.example.playlistmaker.data.impl.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.models.PlaylistInDB
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PlaylistsRepositoryImpl(private val saver: ImageSaver, database: TracksDB) :
    PlaylistsRepository {

    private val dao = database.playlistDao()

    private fun timestamp(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

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
            PlaylistInDB(
                playlistId = 0,
                title = playlist.title,
                description = playlist.description,
                cover = "",
                tracksTotal = 0,
                addedDate = timestamp(),
                lastMod = timestamp()
            )
        )

        saver.saveCover(playlist.coverUri, coverFileName)
    }

    override suspend fun addTrack(track: Track, playlistId: Int): Int {

        return dao.addTrack(track, playlistId)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return dao.getPlaylists()
    }

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override suspend fun containsTrack(playlistId: Int, trackId: Int): Boolean {
        return dao.containsTrack(playlistId, trackId) > 0
    }
}