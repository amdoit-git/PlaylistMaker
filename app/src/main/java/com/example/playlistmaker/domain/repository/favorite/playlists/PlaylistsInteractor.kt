package com.example.playlistmaker.domain.repository.favorite.playlists

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun <T> saveCoverToTmpDir(cover: T): String

    suspend fun createPlaylist(playlist: Playlist): Int

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(id: Int)

    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun countPlaylists(): Int

    suspend fun clearPlaylists()
}