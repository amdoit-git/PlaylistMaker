package com.example.playlistmaker.data.impl.favorite

import android.util.Log
import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.converters.TrackToTrackInDBMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(database: TracksDB) : FavoriteTracksRepository {

    private val tracksCache: MutableSet<Int> = mutableSetOf()
    private val dao = database.favoriteTracksDao()

    override fun containsInCache(trackId: Int):Boolean{
        return tracksCache.contains(trackId)
    }

    private fun loadToCache(vararg trackId: Int) {
        trackId.map {
            tracksCache.add(it)
        }
    }

    private fun loadToCache(tracks:List<Track>) {
        tracks.map {
            tracksCache.add(it.trackId)
        }
    }

    override suspend fun saveTrack(track: Track) {
        dao.saveTrack(TrackToTrackInDBMapper.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        dao.deleteTrack(trackId)
    }

    override suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>> = flow {
        val list = TrackToTrackInDBMapper.map(dao.findTracksById(*trackId))
        emit(list)
    }

    override suspend fun findTrackIds(vararg trackId: Int): Flow<List<Int>> = flow {
        val list = dao.containsTracks(*trackId)
        emit(list)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return dao.getAllTracks().flowOn(Dispatchers.IO).map { TrackToTrackInDBMapper.map(it) }
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> {
        return dao.getAllTracksIds().flowOn(Dispatchers.IO)
    }

    override suspend fun countTracks(): Int {
        return dao.countTracks()
    }

    override suspend fun clearTracks() {
        tracksCache.clear()
        dao.clearTracks()
    }

    override suspend fun connect() {
        dao.doOnConnect()
    }
}