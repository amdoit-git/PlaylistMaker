package com.example.playlistmaker.domain.impl.mediaLibrary.favorite

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    override suspend fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override suspend fun containsTrack(trackId: Int): Flow<Int> {
        return repository.containsTrack(trackId)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()
    }

    override suspend fun clearTracks() {
        repository.clearTracks()
    }

    override suspend fun connect() {
        repository.connect()
    }

}