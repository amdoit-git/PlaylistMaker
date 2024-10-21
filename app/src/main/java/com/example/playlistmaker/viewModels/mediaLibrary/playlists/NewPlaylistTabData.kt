package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import android.net.Uri

sealed interface NewPlaylistTabData {
    data class Cover(val uri: Uri?) : NewPlaylistTabData
    data class Title(val text: String) : NewPlaylistTabData
    data class Description(val text: String) : NewPlaylistTabData
    data class Button(val enabled: Boolean) : NewPlaylistTabData
    data class Close(val allowed: Boolean) : NewPlaylistTabData
    data class ScreenTitle(val text: String) : NewPlaylistTabData
    data class ButtonTitle(val text: String) : NewPlaylistTabData
}