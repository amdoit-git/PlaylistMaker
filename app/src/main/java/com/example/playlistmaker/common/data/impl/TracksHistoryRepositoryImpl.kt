package com.example.playlistmaker.common.data.impl

import android.app.Application.MODE_PRIVATE
import android.content.Context
import com.example.playlistmaker.common.data.APP_SETTINGS_PREFERENCES
import com.example.playlistmaker.common.data.SearchHistory
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.domain.repository.TracksHistoryRepository

class TracksHistoryRepositoryImpl(context: Context) : TracksHistoryRepository {

    init {
        SearchHistory.setSharedPreferences(
            sharedPrefs = context.getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)
        )
    }

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