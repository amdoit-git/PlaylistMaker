package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.common.domain.models.Track

sealed interface SearchData {
    data class TrackList(val state: TrackListState) : SearchData
    data class SearchText(val text: String) : SearchData
    data class ProgressBar(val visible: Boolean) : SearchData
    data class MoveToTop(val track: Track) : SearchData
}