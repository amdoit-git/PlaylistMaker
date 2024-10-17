package com.example.playlistmaker.data.impl.search

import android.util.Log
import com.example.playlistmaker.data.db.converters.TrackToRoomTrackMapper
import com.example.playlistmaker.data.db.dao.HistoryTracksDao
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.TracksHistoryRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TracksHistoryRepositoryImpl(
    private val dao: HistoryTracksDao,
    private val gson: Gson
) :
    TracksHistoryRepository {

    private val LAST_VIEWED_TRACKS = "LAST_VIEWED_TRACKS"
    private val MAX_TRACKS_IN_LIST: Int = 10

    override fun jsonToTracks(json: String): List<Track>? {

        try {
            val type = object : TypeToken<MutableList<Track>>() {}.type
            val tracks: MutableList<Track> = gson.fromJson(json, type)
            tracks.forEachIndexed { index, track ->
                tracks[index] = track.getTrack()
            }
            return tracks
        } catch (er: JsonSyntaxException) {
            er.message?.let {
                Log.d("PM_ERROR", it)
            } ?: run {
                Log.d("PM_ERROR", "jsonToTracks")
            }
        }

        return null
    }

    override suspend fun save(track: Track) {

        dao.saveTrack(TrackToRoomTrackMapper.map(track))
    }

    override suspend fun getAll(): List<Track>? {

        val list: List<Track> = TrackToRoomTrackMapper.map(dao.getAllTracks())

        return list.ifEmpty {
            null
        }
    }

    override suspend fun clear() {
        dao.clearTracks()
    }

    override fun jsonToTrack(json: String): Track? {
        try {
            val track: Track = gson.fromJson(json, Track::class.java)
            return track.getTrack()
        } catch (_: JsonSyntaxException) {

        }
        return null;
    }

    override fun toJson(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }

    override fun toJson(track: Track): String {
        return gson.toJson(track)
    }
}