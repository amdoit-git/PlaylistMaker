package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val coverUrl: String
)
