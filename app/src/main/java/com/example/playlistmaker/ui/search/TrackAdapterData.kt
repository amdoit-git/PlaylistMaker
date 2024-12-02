package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface TrackAdapterData {
    data class TrackClick(val track: Track) : TrackAdapterData
    data class TrackLongClick(val track: Track) : TrackAdapterData
    data class ButtonClick(val clicked: Boolean = true) : TrackAdapterData
    data class ScrollTracksList(val position: Int) : TrackAdapterData
}