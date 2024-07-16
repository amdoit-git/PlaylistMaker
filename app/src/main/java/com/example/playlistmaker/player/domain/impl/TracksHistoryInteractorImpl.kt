package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.repository.TrackHistoryInteractor
import com.example.playlistmaker.player.domain.repository.TracksHistoryRepository

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TrackHistoryInteractor {

    override fun save(track: Track) {
        repository.save(track)
    }

    override fun getList(): List<Track> {
        val list: List<Track> = repository.getList() ?: emptyList()
        return list
    }

    override fun clear() {
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