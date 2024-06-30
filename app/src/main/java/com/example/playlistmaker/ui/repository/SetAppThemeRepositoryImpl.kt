package com.example.playlistmaker.ui.repository

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.repository.SetAppThemeRepository

class SetAppThemeRepositoryImpl() : SetAppThemeRepository {

    override fun setTheme(theme: APP_THEME) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme == APP_THEME.DARK) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}