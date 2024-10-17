package com.example.playlistmaker.data.db.models

data class PlaylistUpdates(
    val playlistId: Int,
    val title: String,
    val description: String,
    val coverFileName: String,
    val lastMod: Int//Время обновления плейлиста
)
