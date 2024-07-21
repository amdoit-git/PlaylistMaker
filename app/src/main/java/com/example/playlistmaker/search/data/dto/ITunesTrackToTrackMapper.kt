package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.common.data.GetStringResource
import com.example.playlistmaker.common.domain.models.Track
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object ITunesTrackToTrackMapper {

    private fun map(item: ItunesTrack): Track {

        return Track(
            trackId = item.trackId,
            trackName = item.trackName ?: "-",
            artistName = item.artistName ?: "-",
            trackTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(item.trackTimeMillis),
            trackCover = item.artworkUrl100 ?: "",
            previewUrl = item.previewUrl ?: "",
            albumName = item.collectionName ?: "-",
            country = GetStringResource.getByName("country_code_" + item.country) ?: "-",
            genre = item.primaryGenreName ?: "-",
            albumYear = getReleaseYear(item.releaseDate) ?: "-"
        )
    }

    private fun getReleaseYear(releaseDate: String?): String? {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        releaseDate?.let {
            try {
                val date = LocalDate.parse(it, format)
                return date.year.toString()
            } catch (_: DateTimeParseException) {
            }
        }

        return null
    }

    fun map(itunesTracks: List<ItunesTrack>): List<Track> {

        val tracks: MutableList<Track> = mutableListOf()

        for (item in itunesTracks) {

            tracks.add(map(item))
        }

        return tracks
    }
}