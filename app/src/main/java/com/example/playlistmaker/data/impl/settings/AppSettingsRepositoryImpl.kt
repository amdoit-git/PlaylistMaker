package com.example.playlistmaker.data.impl.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.AppTheme
import com.example.playlistmaker.domain.repository.settings.AppSettingsRepository

class AppSettingsRepositoryImpl(sharedPreferences: SharedPreferences) : AppSettingsRepository {

    private val DARK_THEME_KEY = "DARK_THEME"

    private val sharedPrefs: SharedPreferences = sharedPreferences

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