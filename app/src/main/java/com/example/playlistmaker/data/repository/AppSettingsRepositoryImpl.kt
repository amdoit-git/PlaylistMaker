package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.repository.AppSettingsRepository

class AppSettingsRepositoryImpl(context: Context) : AppSettingsRepository {

    private val APP_SETTINGS_PREFERENCES = "APP_SETTINGS_PREFERENCES"
    private val DARK_THEME_KEY = "DARK_THEME"

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)

    override fun saveTheme(theme: APP_THEME) {

        val editor = sharedPreferences.edit()

        when (theme) {
            APP_THEME.DEFAULT -> editor.remove(DARK_THEME_KEY).apply()
            APP_THEME.LIGHT -> editor.putBoolean(DARK_THEME_KEY, false).apply()
            APP_THEME.DARK -> editor.putBoolean(DARK_THEME_KEY, true).apply()
        }
    }

    override fun getTheme(): APP_THEME {

        return if (sharedPreferences.contains(DARK_THEME_KEY)) {
            APP_THEME.find(sharedPreferences.getBoolean(DARK_THEME_KEY, false))
        } else {
            APP_THEME.DEFAULT
        }
    }
}