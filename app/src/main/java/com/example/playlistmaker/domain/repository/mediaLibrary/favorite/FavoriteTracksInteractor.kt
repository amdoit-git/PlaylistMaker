package com.example.playlistmaker.domain.repository.mediaLibrary.favorite

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun saveTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun containsTrack(trackId: Int): Flow<Int>

    suspend fun getAllTracks(): Flow<List<Track>>

    suspend fun clearTracks()

    suspend fun connect()
}