package com.example.playlistmaker.viewModels.favorite

import androidx.media3.common.Tracks
import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteData {
    data class TrackList(val tracks: List<Track>) : FavoriteData
    data class MoveToTop(val track: Track) : FavoriteData
    data class OpenPlayerScreen(val track: String) : FavoriteData
    data class ScrollTracksList(val position: Int = 0) : FavoriteData
}