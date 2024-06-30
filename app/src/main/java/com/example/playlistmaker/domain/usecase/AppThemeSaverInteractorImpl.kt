package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.repository.AppSettingsRepository
import com.example.playlistmaker.domain.repository.AppThemeSaverInteractor

class AppThemeSaverInteractorImpl(private val repository: AppSettingsRepository) : AppThemeSaverInteractor {
    override fun getTheme(): APP_THEME {
        return repository.getTheme()
    }

    override fun saveTheme(theme: APP_THEME) {
        repository.saveTheme(theme)
    }
}