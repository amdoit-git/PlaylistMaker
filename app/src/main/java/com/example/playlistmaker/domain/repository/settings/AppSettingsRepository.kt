package com.example.playlistmaker.domain.repository.settings

import com.example.playlistmaker.domain.models.AppTheme

interface AppSettingsRepository {

    fun setTheme(theme: AppTheme)

    fun saveTheme(theme: AppTheme)

    fun getTheme(): AppTheme?

    fun restoreTheme()
}