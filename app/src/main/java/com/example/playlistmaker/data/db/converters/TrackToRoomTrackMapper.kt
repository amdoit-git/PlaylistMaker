package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.models.RoomTrack
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackToRoomTrackMapper {

    private fun yearToString(year: Int): String {
        return year.toString()
    }

    private fun yearToInt(year: String): Int {
        return year.toInt()
    }

    private fun timeToInt(time: String): Int {
        try {
            return time.split(":").let { it[0].toInt() * 60 + it[1].toInt() }
        } catch (er: IndexOutOfBoundsException) {
            return 0
        }
    }

    private fun timeToString(time: Int): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(time * 1000L)
    }

    fun map(track: Track): RoomTrack = RoomTrack(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        duration = timeToInt(track.trackTime),
        trackCover = track.trackCover,
        previewUrl = track.previewUrl,
        albumName = track.albumName,
        albumYear = yearToInt(track.albumYear),
        genre = track.genre,
        country = track.country
    )

    fun map(track: RoomTrack): Track = Track(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTime = timeToString(track.duration),
        trackCover = track.trackCover,
        previewUrl = track.previewUrl,
        albumName = track.albumName,
        albumYear = yearToString(track.albumYear),
        genre = track.genre,
        country = track.country
    )

    fun mapWithNull(track: RoomTrack?): Track? {

        track?.let {
            return map(it)
        }

        return null
    }

    fun map(tracks: List<RoomTrack>): List<Track> = tracks.map(::map)
}