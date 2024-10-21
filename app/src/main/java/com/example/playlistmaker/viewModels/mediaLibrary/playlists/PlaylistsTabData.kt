package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistsTabData {
    data class Playlists(val playlists: List<Playlist>) : PlaylistsTabData
}