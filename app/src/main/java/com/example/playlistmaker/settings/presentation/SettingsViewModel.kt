package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.models.AppTheme
import com.example.playlistmaker.common.domain.models.EmailData
import com.example.playlistmaker.common.domain.models.ShareData
import com.example.playlistmaker.common.domain.repository.AppSettingsInteractor
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorInteractor

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

//    class Factory(private val application: Application) :
//        ViewModelProvider.AndroidViewModelFactory(application) {
//
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return SettingsViewModel(application) as T
//        }
//    }
}