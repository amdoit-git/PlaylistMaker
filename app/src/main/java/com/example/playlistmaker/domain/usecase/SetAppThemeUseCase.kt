package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.repository.SetAppThemeRepository

class SetAppThemeUseCase(private val repository: SetAppThemeRepository) {

    fun execute(theme: APP_THEME) {
        repository.setTheme(theme)
    }
}