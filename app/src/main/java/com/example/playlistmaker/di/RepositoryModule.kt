package com.example.playlistmaker.di

import com.example.playlistmaker.common.data.impl.AppSettingsRepositoryImpl
import com.example.playlistmaker.common.data.impl.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.common.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.common.domain.repository.AppSettingsRepository
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.common.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.data.impl.ITunesRepositoryImpl
import com.example.playlistmaker.search.domain.repository.ITunesRepository
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
