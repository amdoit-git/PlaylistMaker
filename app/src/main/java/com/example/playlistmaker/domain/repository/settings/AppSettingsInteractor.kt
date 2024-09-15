package com.example.playlistmaker.domain.repository.settings

import com.example.playlistmaker.domain.models.AppTheme

interface AppSettingsInteractor {

    fun setTheme(theme: AppTheme)

    suspend fun saveTheme(theme: AppTheme)

    suspend fun getTheme(): AppTheme?

    suspend fun restoreTheme()
}