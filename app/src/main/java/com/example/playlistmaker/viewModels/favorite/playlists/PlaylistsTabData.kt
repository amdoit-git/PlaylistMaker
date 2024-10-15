package com.example.playlistmaker.viewModels.favorite.playlists

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistsTabData {
    data class Playlists(val playlists: List<Playlist>) : PlaylistsTabData
    data class Error(val message:String) : PlaylistsTabData
}