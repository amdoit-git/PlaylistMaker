package com.example.playlistmaker.data.impl.favorite

import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.converters.TrackToTrackInDBMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(database: TracksDB) : FavoriteTracksRepository {

    private val dao = database.favoriteTracksDao()

    override suspend fun saveTrack(track: Track) {
        dao.saveTrack(TrackToTrackInDBMapper.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        dao.deleteTrack(trackId)
    }

    override suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>> = flow {
        emit(TrackToTrackInDBMapper.map(dao.findTracksById(*trackId)))
    }

    override suspend fun containsTracks(vararg trackId: Int): Flow<List<Int>> = flow {
        emit(dao.containsTracks(*trackId))
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        emit(TrackToTrackInDBMapper.map(dao.getAllTracks()))
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> = flow {
        emit(dao.getAllTracksIds())
    }

    override suspend fun countTracks(): Int {
        return dao.countTracks()
    }

    override suspend fun clearTracks() {
        dao.clearTracks()
    }

    override suspend fun connect() {
        dao.getDBVersion()
    }
}