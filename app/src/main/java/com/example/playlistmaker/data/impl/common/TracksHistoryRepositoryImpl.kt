package com.example.playlistmaker.data.impl.common

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TracksHistoryRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TracksHistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
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
        val tracks: MutableList<Track> =
            mutableListOf(track.copy(isPlaying = false, inFavorite = false, isLiked = false))

        getList()?.let {
            val loadedTracks = it.filter { t -> t.trackId != track.trackId }

            for (i in 0 until minOf(MAX_TRACKS_IN_LIST - 1, loadedTracks.size)) {

                tracks.add(loadedTracks[i])
            }
        }
        withContext(Dispatchers.IO) {
            sharedPrefs.edit().putString(LAST_VIEWED_TRACKS, toJson(tracks)).commit()
        }
    }

    override suspend fun getList(): List<Track>? {

        return withContext(Dispatchers.IO) {

            sharedPrefs.getString(LAST_VIEWED_TRACKS, null)?.let { json ->
                return@withContext jsonToTracks(json)
            }

            return@withContext null
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            sharedPrefs.edit().remove(LAST_VIEWED_TRACKS).commit()
        }
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