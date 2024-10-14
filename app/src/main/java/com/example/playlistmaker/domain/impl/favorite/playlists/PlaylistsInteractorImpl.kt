package com.example.playlistmaker.domain.impl.favorite.playlists

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {

    override suspend fun <T> saveCoverToTmpDir(cover: T): Uri {
        return repository.saveCoverToTmpDir(cover)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        return repository.addPlaylist(playlist)
    }

    override suspend fun addTrack(track: Track, playlistId: Int): Int {
        return repository.addTrack(track, playlistId)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getTracks(playlistId)
    }

    override suspend fun containsTrack(playlistId: Int, trackId: Int): Boolean {
        return repository.containsTrack(playlistId, trackId)
    }
}