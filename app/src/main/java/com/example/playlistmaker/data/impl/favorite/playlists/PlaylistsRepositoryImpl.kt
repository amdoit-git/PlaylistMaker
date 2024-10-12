package com.example.playlistmaker.data.impl.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PlaylistsRepositoryImpl(private val saver: ImageSaver, database: TracksDB) : PlaylistsRepository {

    private val dao = database.favoriteTracksDao()

    override suspend fun <T> saveCoverToTmpDir(cover: T): String {

        if (cover is Bitmap) {
            return saver.saveToTmpStorage(cover).toString()
        }

        if (cover is Uri) {
            return saver.saveToTmpStorage(cover).toString()
        }

        if (cover is File) {
            return saver.saveToTmpStorage(cover).toString()
        }

        return ""
    }

    override suspend fun createPlaylist(playlist: Playlist): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        TODO("Not yet implemented")
    }

    override suspend fun countPlaylists(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun clearPlaylists() {
        TODO("Not yet implemented")
    }
}