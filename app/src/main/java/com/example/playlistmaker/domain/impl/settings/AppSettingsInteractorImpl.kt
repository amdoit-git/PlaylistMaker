package com.example.playlistmaker.domain.impl.settings

import com.example.playlistmaker.domain.models.AppTheme
import com.example.playlistmaker.domain.repository.settings.AppSettingsInteractor
import com.example.playlistmaker.domain.repository.settings.AppSettingsRepository

class AppSettingsInteractorImpl(private val repository: AppSettingsRepository) :
    AppSettingsInteractor {

    override fun setTheme(theme: AppTheme) {
        repository.setTheme(theme)
    }

    override fun saveTheme(theme: AppTheme) {
        repository.saveTheme(theme)
    }

    override fun getTheme(): AppTheme? {
        return repository.getTheme()
    }

    override fun restoreTheme() {
        repository.restoreTheme()
    }
}