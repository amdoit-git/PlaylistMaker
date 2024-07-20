package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.common.data.SearchHistory

const val APP_SETTINGS_PREFERENCES = "APP_SETTINGS_PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME"

class App : Application() {

    private var darkTheme: Boolean? = null
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)
        restoreTheme()
        SearchHistory.setSharedPreferences(sharedPrefs)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun restoreTheme() {

        if (sharedPrefs.contains(DARK_THEME_KEY)) {
            switchTheme(sharedPrefs.getBoolean(DARK_THEME_KEY, false))
        }
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()
    }
}