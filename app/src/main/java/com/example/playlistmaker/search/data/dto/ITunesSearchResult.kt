package com.example.playlistmaker.search.data.dto

sealed interface ITunesSearchResult {
    data class Data(val list: ItunesTrackList) : ITunesSearchResult
    data class Error(val code: Int, val message: String) : ITunesSearchResult
}