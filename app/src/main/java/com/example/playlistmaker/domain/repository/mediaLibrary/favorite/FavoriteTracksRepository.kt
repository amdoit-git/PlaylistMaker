package com.example.playlistmaker.domain.repository.mediaLibrary.favorite

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun saveTrack(track: Track)

    suspend fun deleteTrack(trackId: Int)

    suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>>

    suspend fun findTrackIds(vararg trackId: Int): Flow<List<Int>>

    suspend fun getAllTracks(): Flow<List<Track>>

    suspend fun getAllTracksIds(): Flow<List<Int>>

    suspend fun countTracks(): Int

    suspend fun clearTracks()

    suspend fun connect()
}