package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.common.domain.models.Track
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class SearchHistory {

    companion object {

        const val LAST_VIEWED_TRACKS = "LAST_VIEWED_TRACKS"
        private const val MAX_TRACKS_IN_LIST: Int = 10
        private var sharedPrefs: SharedPreferences? = null
        private val gson = Gson()

        fun setSharedPreferences(sharedPrefs: SharedPreferences) {
            Companion.sharedPrefs = sharedPrefs
        }

        fun jsonToTracks(json: String): List<Track>? {

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

        fun jsonToTrack(json: String): Track?{
            try {
                val track: Track = gson.fromJson(json, Track::class.java)
                return track.getTrack()
            } catch (_: JsonSyntaxException) {

            }
            return null;
        }

        fun loadTracksList(): List<Track>? {

            sharedPrefs?.let { it ->

                it.getString(LAST_VIEWED_TRACKS, null)?.let { json -> return jsonToTracks(json) }
            }

            return null
        }

        fun saveTrackInList(track: Track) {

            val tracks: MutableList<Track> = mutableListOf(track.copy(isPlaying = false, inFavorite = false, isLiked = false))

            loadTracksList()?.let {
                val loadedTracks = it.filter { t -> t.trackId != track.trackId }

                for (i in 0 until minOf(MAX_TRACKS_IN_LIST - 1, loadedTracks.size)) {

                    tracks.add(loadedTracks[i])
                }
            }

            sharedPrefs?.let {
                it.edit().putString(LAST_VIEWED_TRACKS, toJson(tracks)).apply()
            }
        }

        fun clearHistory() {
            sharedPrefs?.let {
                it.edit().remove(LAST_VIEWED_TRACKS).apply()
            }
        }

        fun toJson(tracks: List<Track>): String {
            return gson.toJson(tracks)
        }

        fun toJson(track: Track): String {
            return gson.toJson(track)
        }
    }
}