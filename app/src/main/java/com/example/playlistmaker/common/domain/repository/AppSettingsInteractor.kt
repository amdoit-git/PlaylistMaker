package com.example.playlistmaker.common.domain.repository

import com.example.playlistmaker.common.domain.models.AppTheme

interface AppSettingsInteractor {

    fun setTheme(theme: AppTheme)

    fun saveTheme(theme: AppTheme)

    fun getTheme(): AppTheme?

    fun restoreTheme()
}