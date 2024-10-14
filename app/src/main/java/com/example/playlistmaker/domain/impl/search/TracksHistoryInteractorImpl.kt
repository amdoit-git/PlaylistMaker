package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryRepository

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TracksHistoryInteractor {

    override suspend fun save(track: Track) {
        repository.save(track)
    }

    override suspend fun getAll(): List<Track> {
        val list: List<Track> = repository.getAll() ?: emptyList()
        return list
    }

    override suspend fun clear() {
        repository.clear()
    }

    override fun jsonToTrack(json: String): Track? {
        return repository.jsonToTrack(json)
    }

    override fun jsonToTracks(json: String): List<Track>? {
        return repository.jsonToTracks(json)
    }

    override fun toJson(tracks: List<Track>): String {
        return repository.toJson(tracks)
    }

    override fun toJson(track: Track): String {
        return repository.toJson(track)
    }
}