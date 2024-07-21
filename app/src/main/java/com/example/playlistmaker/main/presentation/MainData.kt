package com.example.playlistmaker.main.presentation

import com.example.playlistmaker.common.presentation.SCREEN_NAME

sealed interface MainData {
    data class OpenScreen(val name: SCREEN_NAME) : MainData
}