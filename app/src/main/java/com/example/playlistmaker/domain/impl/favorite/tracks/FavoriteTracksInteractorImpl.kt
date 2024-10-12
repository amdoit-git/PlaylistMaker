package com.example.playlistmaker.domain.impl.favorite.tracks

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.tracks.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.favorite.tracks.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override fun containsInCache(trackId: Int): Boolean {
        return repository.containsInCache(trackId)
    }

    override suspend fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>> {
        return repository.findTracksById(*trackId)
    }

    override suspend fun findTrackIds(vararg trackId: Int): Flow<List<Int>> {
        return repository.findTrackIds(*trackId)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> {
        return repository.getAllTracksIds()
    }

    override suspend fun countTracks(): Int {
        return repository.countTracks()
    }

    override suspend fun clearTracks() {
        repository.clearTracks()
    }

    override suspend fun connect() {
        repository.connect()
    }

}