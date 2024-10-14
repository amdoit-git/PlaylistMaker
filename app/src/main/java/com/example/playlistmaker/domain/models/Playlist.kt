package com.example.playlistmaker.domain.models

import android.net.Uri

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val coverUri: Uri?
)
