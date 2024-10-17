package com.example.playlistmaker.ui.common

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.uiModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.settings.AppSettingsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {

        super.onCreate()

        // Функция, которая настраивает библиотеку Koin, нужно вызвать перед использованием
        startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(repositoryModule, interactorModule, viewModelModule, uiModule, dataModule)
        }

        val settings: AppSettingsInteractor by inject()

        runBlocking(Dispatchers.IO) {
            settings.restoreTheme()
        }

        val favorite: FavoriteTracksInteractor by inject()

        GlobalScope.launch(Dispatchers.IO) {
            favorite.connect()
        }
    }
}