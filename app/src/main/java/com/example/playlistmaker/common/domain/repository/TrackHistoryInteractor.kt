package com.example.playlistmaker.common.domain.repository

import com.example.playlistmaker.common.domain.models.Track

interface TrackHistoryInteractor {

    fun save(track: Track)

    fun getList(): List<Track>

    fun clear()

    fun jsonToTrack(json: String): Track?

    fun jsonToTracks(json: String): List<Track>?

    fun toJson(tracks: List<Track>): String

    fun toJson(track: Track): String
}