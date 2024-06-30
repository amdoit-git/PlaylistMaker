package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.APP_THEME

interface SetAppThemeRepository {
    fun setTheme(theme:APP_THEME)
}