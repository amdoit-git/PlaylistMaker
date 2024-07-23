package com.example.playlistmaker.common.data.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.common.data.APP_SETTINGS_PREFERENCES
import com.example.playlistmaker.common.domain.models.AppTheme
import com.example.playlistmaker.common.domain.repository.AppSettingsRepository

class AppSettingsRepositoryImpl(context: Context) : AppSettingsRepository {

    private val DARK_THEME_KEY = "DARK_THEME"

    private val sharedPrefs = context.getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)

    override fun setTheme(theme: AppTheme) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme.dark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun saveTheme(theme: AppTheme) {
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, theme.dark).apply()
    }

    override fun getTheme(): AppTheme? {

        if (sharedPrefs.contains(DARK_THEME_KEY)) {
            return AppTheme(
                dark = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
            )
        }

        return null
    }

    override fun restoreTheme() {

        getTheme()?.let {
            setTheme(it)
        }
    }
}