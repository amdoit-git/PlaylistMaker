package com.example.playlistmaker.common

import android.app.Application
import com.example.playlistmaker.common.data.APP_SETTINGS_PREFERENCES
import com.example.playlistmaker.common.data.GetStringResource
import com.example.playlistmaker.common.data.SearchHistory
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {

        super.onCreate()

        SearchHistory.setSharedPreferences(
            sharedPrefs = getSharedPreferences(APP_SETTINGS_PREFERENCES, MODE_PRIVATE)
        )

        GetStringResource.setApplication(this)

        val settings = Creator.provideAppSettingsInteractor(this.applicationContext)

        settings.restoreTheme()
    }
}