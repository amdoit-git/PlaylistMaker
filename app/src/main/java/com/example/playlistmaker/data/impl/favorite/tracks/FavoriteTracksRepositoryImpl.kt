package com.example.playlistmaker.data.impl.favorite.tracks

import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.converters.TrackToRoomTrackMapper
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.tracks.FavoriteTracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(private val dao: FavoriteTracksDao) : FavoriteTracksRepository {

    override suspend fun saveTrack(track: Track) {
        dao.saveTrack(TrackToRoomTrackMapper.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        dao.deleteTrack(trackId)
    }

    override suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>> = flow {
        val list = TrackToRoomTrackMapper.map(dao.findTracksById(*trackId))
        emit(list)
    }

    override suspend fun findTrackIds(vararg trackId: Int): Flow<List<Int>> = flow {
        val list = dao.containsTracks(*trackId)
        emit(list)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return dao.getAllTracks().flowOn(Dispatchers.IO).distinctUntilChanged().map { TrackToRoomTrackMapper.map(it) }
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> {
        return dao.getAllTracksIds().flowOn(Dispatchers.IO)
    }

    override suspend fun countTracks(): Int {
        return dao.countTracks()
    }

    override suspend fun clearTracks() {
         dao.clearTracks()
    }

    override suspend fun connect() {
        dao.doOnConnect()
    }
}