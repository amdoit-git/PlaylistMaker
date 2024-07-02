package com.example.playlistmaker.data.repository

import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TracksHistoryRepository

class TracksHistoryRepositoryImpl() : TracksHistoryRepository {

    override fun save(track: Track) {
        SearchHistory.saveTrackInList(track)
    }

    override fun getList(): List<Track>? {
        return SearchHistory.loadTracksList()
    }

    override fun clear() {
        SearchHistory.clearHistory()
    }

    override fun jsonToTrack(json: String): Track? {
        return SearchHistory.jsonToTrack(json)
    }

    override fun jsonToTracks(json: String): List<Track>? {
        return SearchHistory.jsonToTracks(json)
    }

    override fun toJson(tracks: List<Track>): String {
        return SearchHistory.toJson(tracks)
    }

    override fun toJson(track: Track): String {
        return SearchHistory.toJson(track)
    }
}