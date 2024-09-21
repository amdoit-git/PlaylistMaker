package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.models.TrackInDB
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackToTrackInDBMapper {

    private fun yearToString(year: Int): String {
        return year.toString()
    }

    private fun yearToInt(year: String): Int {
        return year.toInt()
    }

    private fun timeToInt(time: String): Int {
        return time.split(":").let { it[0].toInt() + it[1].toInt() * 60 }
    }

    private fun timeToString(time: Int): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(time * 1000L)
    }

    fun map(track: Track): TrackInDB = TrackInDB(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTime = timeToInt(track.trackTime),
        trackCover = track.trackCover,
        previewUrl = track.previewUrl,
        albumName = track.albumName,
        albumYear = yearToInt(track.albumYear),
        genre = track.genre,
        country = track.country
    )

    fun map(track: TrackInDB): Track = Track(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTime = yearToString(track.trackTime),
        trackCover = track.trackCover,
        previewUrl = track.previewUrl,
        albumName = track.albumName,
        albumYear = yearToString(track.albumYear),
        genre = track.genre,
        country = track.country
    )

    fun map(tracks: List<TrackInDB>): List<Track> = tracks.map(::map)
}