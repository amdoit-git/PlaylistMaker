package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TracksHistoryRepository

class TracksHistoryRepositoryImpl(context: Context) : TracksHistoryRepository {

    private val APP_SETTINGS_PREFERENCES = "APP_SETTINGS_PREFERENCES"

    init {

        val sharedPreferences =
            context.applicationContext.getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)

        SearchHistory.setSharedPreferences(sharedPreferences)
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