package com.example.playlistmaker.viewModels.search

import com.example.playlistmaker.domain.models.Track

sealed interface SearchData {
    data class TrackList(val state: TrackListState) : SearchData
    data class SearchText(val text: String, val textInFocus: Boolean) : SearchData
    data class ProgressBar(val visible: Boolean) : SearchData
    data class MoveToTop(val track: Track) : SearchData
    data class OpenPlayerScreen(val track: String) : SearchData
}