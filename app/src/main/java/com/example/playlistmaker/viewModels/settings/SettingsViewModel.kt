package com.example.playlistmaker.viewModels.settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.AppTheme
import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.domain.models.ShareData
import com.example.playlistmaker.domain.repository.settings.AppSettingsInteractor
import com.example.playlistmaker.domain.repository.ExternalNavigatorInteractor

class SettingsViewModel(
    private val navigator: ExternalNavigatorInteractor,
    private val settings: AppSettingsInteractor
) : ViewModel() {

    fun share(text: String) {
        navigator.share(ShareData(text = text), true)
    }

    fun openUrl(url: String) {
        navigator.openUrl(url)
    }

    fun sendEmail(data: EmailData) {
        navigator.sendEmail(data)
    }

    fun switchTheme(isDarkTheme: Boolean) {
        settings.setTheme(AppTheme(isDarkTheme))
        settings.saveTheme(AppTheme(isDarkTheme))
    }
}