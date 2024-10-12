package com.example.playlistmaker.domain.impl.favorite.playlists

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {

    override suspend fun <T> saveCoverToTmpDir(cover: T): String {
        return repository.saveCoverToTmpDir(cover)
    }

    override suspend fun createPlaylist(playlist: Playlist): Int {
        return repository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Int) {
        repository.deletePlaylist(id)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun countPlaylists(): Int {
        return repository.countPlaylists()
    }

    override suspend fun clearPlaylists() {
        repository.clearPlaylists()
    }
}