package com.example.playlistmaker.player.presentation

data class TrackProgress(
    var time: String? = null,
    var duration: String? = null,
    var stopped: Boolean? = null
)