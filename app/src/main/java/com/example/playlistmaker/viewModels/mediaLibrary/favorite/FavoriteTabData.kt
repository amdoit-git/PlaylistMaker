package com.example.playlistmaker.viewModels.mediaLibrary.favorite

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteTabData {
    data class TrackList(val tracks: List<Track>) : FavoriteTabData
    data class MoveToTop(val track: Track) : FavoriteTabData
    data class OpenPlayerScreen(val track: String) : FavoriteTabData
    data class ScrollTracksList(val position: Int = 0) : FavoriteTabData
}