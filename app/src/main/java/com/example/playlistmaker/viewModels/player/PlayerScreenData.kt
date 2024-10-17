package com.example.playlistmaker.viewModels.player

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track

sealed interface PlayerScreenData {
    data class TrackData(val track: Track) : PlayerScreenData

    data class TrackProgress(
        var time: String? = null,
        var duration: String? = null,
        var stopped: Boolean? = null
    ) : PlayerScreenData

    data class FavoriteStatus(val isFavorite: Boolean) : PlayerScreenData

    data class Playlists(val playlists: List<Playlist>) : PlayerScreenData

    data class PlaylistNotFound(val message: String) : PlayerScreenData

    data class BottomSheet(val opened:Boolean) : PlayerScreenData
}