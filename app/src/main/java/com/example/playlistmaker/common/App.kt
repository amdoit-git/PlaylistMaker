package com.example.playlistmaker.common

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {

        super.onCreate()

        val settings = Creator.provideAppSettingsInteractor(this.applicationContext)

        settings.restoreTheme()
    }
}