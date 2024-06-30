package com.example.playlistmaker.ui

import android.app.Application
import com.example.playlistmaker.data.repository.AppSettingsRepositoryImpl
import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecase.AppThemeSaverInteractorImpl
import com.example.playlistmaker.domain.usecase.SetAppThemeUseCase
import com.example.playlistmaker.ui.repository.SetAppThemeRepositoryImpl

const val APP_SETTINGS_PREFERENCES = "APP_SETTINGS_PREFERENCES"
const val DARK_THEME_KEY = "DARK_THEME"

class App : Application() {

    private val repository by lazy { AppSettingsRepositoryImpl(this) }
    private val appThemeSaver by lazy { AppThemeSaverInteractorImpl(repository = repository) }
    private val setTheme by lazy { SetAppThemeUseCase(repository = SetAppThemeRepositoryImpl()) }

    var selectedTrack: Track? = null

    override fun onCreate() {
        super.onCreate()
        restoreTheme()
    }

    private fun restoreTheme() {

        val theme = appThemeSaver.getTheme()

        if (theme != APP_THEME.DEFAULT) {
            setTheme.execute(theme)
        }
    }
}