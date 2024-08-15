package com.example.playlistmaker.viewModels.player

import com.example.playlistmaker.domain.models.Track

sealed interface PlayerScreenData {
    data class TrackData(val track: Track) : PlayerScreenData

    data class TrackProgress(
        var time: String? = null,
        var duration: String? = null,
        var stopped: Boolean? = null
    ) : PlayerScreenData

    data class ToastMessage(val message: String, val isVisible: Boolean) : PlayerScreenData
}