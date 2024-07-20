package com.example.playlistmaker.search.presentation

sealed interface SearchData {
    data class TrackList(val state: TRACK_LIST_STATE) : SearchData
    data class SearchText(val text: String) : SearchData
    data class ProgressBar(val visible:Boolean) : SearchData
}