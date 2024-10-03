package com.example.playlistmaker.di

import com.example.playlistmaker.data.impl.favorite.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.impl.settings.AppSettingsRepositoryImpl
import com.example.playlistmaker.data.impl.settings.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.impl.search.TracksHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.settings.AppSettingsRepository
import com.example.playlistmaker.domain.repository.settings.ExternalNavigatorRepository
import com.example.playlistmaker.domain.repository.search.TracksHistoryRepository
import com.example.playlistmaker.data.impl.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.repository.player.MediaPlayerRepository
import com.example.playlistmaker.data.impl.search.ITunesRepositoryImpl
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksRepository
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

    single <FavoriteTracksRepository>{
        FavoriteTracksRepositoryImpl(database = get())
    }
}
