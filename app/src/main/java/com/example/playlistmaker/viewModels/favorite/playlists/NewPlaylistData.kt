package com.example.playlistmaker.viewModels.favorite.playlists

import android.net.Uri

sealed interface NewPlaylistData {
    data class Cover(val uri: Uri?) : NewPlaylistData
    data class Title(val text: String) : NewPlaylistData
    data class Description(val text: String) : NewPlaylistData
    data class Button(val enabled: Boolean) : NewPlaylistData
    data class Close(val allowed:Boolean) : NewPlaylistData
}