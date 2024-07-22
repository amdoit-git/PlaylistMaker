package com.example.playlistmaker.search.data.dto

import android.content.Context
import android.content.res.Resources
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object ITunesTrackToTrackMapper {

    private fun map(item: ItunesTrack, context: Context): Track {

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
            country = getCountryByCode("country_code_" + item.country, context),
            genre = item.primaryGenreName ?: "-",
            albumYear = getReleaseYear(item.releaseDate) ?: "-"
        )
    }

    private fun getCountryByCode(code: String, context: Context): String {
        try {
            val resId: Int = context.resources.getIdentifier(code, "string", context.packageName);
            return context.getString(resId)
        } catch (_: Resources.NotFoundException) {
        }
        return context.getString(R.string.unknown_country)
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

    fun map(itunesTracks: List<ItunesTrack>, context: Context): List<Track> {

        val tracks: MutableList<Track> = mutableListOf()

        for (item in itunesTracks) {

            tracks.add(map(item, context))
        }

        return tracks
    }
}