package com.example.playlistmaker.viewModels.main

import com.example.playlistmaker.viewModels.player.PlayerScreenData

sealed interface MainActivityData {
    data class ToastMessage(val message: String, val isVisible: Boolean) : MainActivityData
}