package com.example.playlistmaker.data.impl.favorite

import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.db.converters.TrackToTrackInDBMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
        loadToCache(track.trackId)
        dao.saveTrack(TrackToTrackInDBMapper.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        tracksCache.remove(trackId)
        dao.deleteTrack(trackId)
    }

    override suspend fun findTracksById(vararg trackId: Int): Flow<List<Track>> = flow {
        val list = TrackToTrackInDBMapper.map(dao.findTracksById(*trackId))
        loadToCache(list)
        emit(list)
    }

    override suspend fun findTrackIds(vararg trackId: Int): Flow<List<Int>> = flow {
        val list = dao.containsTracks(*trackId)
        loadToCache(*list.toIntArray())
        emit(list)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> = flow {
        val list = TrackToTrackInDBMapper.map(dao.getAllTracks())
        loadToCache(list)
        emit(list)
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> = flow {
        val list = dao.getAllTracksIds()
        loadToCache(*list.toIntArray())
        emit(list)
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