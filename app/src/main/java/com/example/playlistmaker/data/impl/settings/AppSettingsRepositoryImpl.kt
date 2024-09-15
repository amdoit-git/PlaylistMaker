package com.example.playlistmaker.data.impl.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.AppTheme
import com.example.playlistmaker.domain.repository.settings.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun saveTheme(theme: AppTheme) {
        withContext(Dispatchers.IO) {
            sharedPrefs.edit().putBoolean(DARK_THEME_KEY, theme.dark).commit()
        }
    }

    override suspend fun getTheme(): AppTheme? {

        return withContext(Dispatchers.IO) {

            if (sharedPrefs.contains(DARK_THEME_KEY)) {
                AppTheme(
                    dark = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
                )
            } else {
                null
            }
        }
    }

    override suspend fun restoreTheme() {

        getTheme()?.let {
            setTheme(it)
        }
    }
}