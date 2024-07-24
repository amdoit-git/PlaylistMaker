package com.example.playlistmaker.common

import android.app.Application
import com.example.playlistmaker.common.domain.repository.AppSettingsInteractor
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.repositoryModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {



    override fun onCreate() {

        super.onCreate()

        //val settings = Creator.provideAppSettingsInteractor(this.applicationContext)

        // Функция, которая настраивает библиотеку Koin, нужно вызвать перед использованием
        startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(repositoryModule)
        }

        val settings: AppSettingsInteractor by inject()

        settings.restoreTheme()
    }
}