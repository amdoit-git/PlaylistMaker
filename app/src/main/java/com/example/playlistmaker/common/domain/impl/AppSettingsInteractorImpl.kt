package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.models.AppTheme
import com.example.playlistmaker.common.domain.repository.AppSettingsInteractor
import com.example.playlistmaker.common.domain.repository.AppSettingsRepository

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