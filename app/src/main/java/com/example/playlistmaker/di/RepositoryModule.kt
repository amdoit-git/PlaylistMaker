package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlistmaker.common.data.APP_SETTINGS_PREFERENCES
import com.example.playlistmaker.common.data.impl.AppSettingsRepositoryImpl
import com.example.playlistmaker.common.data.impl.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.common.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.common.domain.impl.AppSettingsInteractorImpl
import com.example.playlistmaker.common.domain.impl.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.common.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.common.domain.repository.AppSettingsInteractor
import com.example.playlistmaker.common.domain.repository.AppSettingsRepository
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorInteractor
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.common.domain.repository.TrackHistoryInteractor
import com.example.playlistmaker.common.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.data.impl.ITunesRepositoryImpl
import com.example.playlistmaker.search.domain.impl.ITunesInteractorImpl
import com.example.playlistmaker.search.domain.repository.ITunesInteractor
import com.example.playlistmaker.search.domain.repository.ITunesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(repository = get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(mediaPlayer = get())
    }

    factory<TrackHistoryInteractor> {
        TracksHistoryInteractorImpl(repository = get())
    }

    factory<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(sharedPreferences = get())
    }

    factory<ITunesInteractor> {
        ITunesInteractorImpl(repository = get())
    }

    factory<ITunesRepository> {
        ITunesRepositoryImpl(context = androidContext())
    }

    factory<ExternalNavigatorInteractor> {
        ExternalNavigatorInteractorImpl(repository = get())
    }

    factory<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = androidContext())
    }

    factory<AppSettingsInteractor> {
        AppSettingsInteractorImpl(repository = get())
    }

    factory<AppSettingsRepository> {
        AppSettingsRepositoryImpl(sharedPreferences = get())
    }

    single {
        MediaPlayer()
    }

    single {
        androidContext().getSharedPreferences(
                APP_SETTINGS_PREFERENCES, MODE_PRIVATE
        )
    }

    factory<MyThread>{
        MyThread()
    }
}

class MyThread : Thread(){

    override fun run() {
        super.run()
    }
}
