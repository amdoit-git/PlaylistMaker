package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.APP_THEME

//сохранение настроек приложения в формате ключ / значение

interface AppSettingsRepository {

    fun saveTheme(theme: APP_THEME)

    fun getTheme(): APP_THEME
}