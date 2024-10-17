package com.example.playlistmaker.data.impl.mediaLibrary.favorite

import com.example.playlistmaker.data.db.converters.TrackToRoomTrackMapper
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksRepository
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

    override suspend fun containsTrack(trackId: Int): Flow<Int> = flow {
        emit(dao.containsTracks(trackId))
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return dao.getAllTracks().flowOn(Dispatchers.IO).distinctUntilChanged().map { TrackToRoomTrackMapper.map(it) }
    }

    override suspend fun clearTracks() {
         dao.clearTracks()
    }

    override suspend fun connect() {
        dao.doOnAppStart()
    }
}