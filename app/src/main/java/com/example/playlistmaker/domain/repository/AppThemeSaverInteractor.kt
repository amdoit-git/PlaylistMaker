package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.APP_THEME

interface AppThemeSaverInteractor {

    fun getTheme():APP_THEME

    fun saveTheme(theme: APP_THEME)
}