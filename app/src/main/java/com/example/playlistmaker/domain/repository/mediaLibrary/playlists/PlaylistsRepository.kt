package com.example.playlistmaker.domain.repository.mediaLibrary.playlists

import android.net.Uri
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun <T> saveCoverToTmpDir(cover: T): Uri

    suspend fun addNewPlaylist(playlist: Playlist)

    suspend fun addTrack(track: Track, playlistId: Int)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getTracks(playlistId: Int): Flow<List<Track>>

    suspend fun containsTrack(playlistId: Int, trackId: Int): Boolean
}