package com.example.playlistmaker.di

import com.example.playlistmaker.data.impl.settings.AppSettingsRepositoryImpl
import com.example.playlistmaker.data.impl.common.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.impl.common.TracksHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.settings.AppSettingsRepository
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.data.impl.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.repository.player.MediaPlayerRepository
import com.example.playlistmaker.data.impl.search.ITunesRepositoryImpl
import com.example.playlistmaker.domain.repository.search.ITunesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(mediaPlayerService = get())
    }

    factory<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(sharedPrefs = get(), gson = get())
    }

    factory<ITunesRepository> {
        ITunesRepositoryImpl(context = androidContext(), api = get())
    }

    factory<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = androidContext())
    }

    factory<AppSettingsRepository> {
        AppSettingsRepositoryImpl(sharedPreferences = get())
    }
}
