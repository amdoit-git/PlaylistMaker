package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {

    fun save(track: Track)

    fun getList(): List<Track>

    fun clear()

    fun jsonToTrack(json: String): Track?

    fun jsonToTracks(json: String): List<Track>?

    fun toJson(tracks: List<Track>): String

    fun toJson(track: Track): String
}