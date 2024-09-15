package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryRepository {

    suspend fun save(track: Track)

    suspend fun getList():List<Track>?

    suspend fun clear()

    fun jsonToTrack(json: String): Track?

    fun jsonToTracks(json: String): List<Track>?

    fun toJson(tracks: List<Track>): String

    fun toJson(track: Track): String
}