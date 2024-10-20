package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistScreenData {
    data class Info(val playlist: Playlist) : PlaylistScreenData
    data class Tracks(val tracks: List<Track>) : PlaylistScreenData
    data class MenuBsState(val opened:Boolean) : PlaylistScreenData
    data class TracksBsState(val opened:Boolean) : PlaylistScreenData
    data class GoBack(val allowed:Boolean) : PlaylistScreenData
}