package com.example.playlistmaker.di

import com.example.playlistmaker.common.domain.impl.AppSettingsInteractorImpl
import com.example.playlistmaker.common.domain.impl.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.common.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.domain.repository.AppSettingsInteractor
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorInteractor
import com.example.playlistmaker.common.domain.repository.TracksHistoryInteractor
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerInteractor
import com.example.playlistmaker.search.domain.impl.ITunesInteractorImpl
import com.example.playlistmaker.search.domain.repository.ITunesInteractor
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(repository = get())
    }

    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(repository = get())
    }

    factory<ITunesInteractor> {
        ITunesInteractorImpl(repository = get())
    }

    factory<ExternalNavigatorInteractor> {
        ExternalNavigatorInteractorImpl(repository = get())
    }

    factory<AppSettingsInteractor> {
        AppSettingsInteractorImpl(repository = get())
    }

    factory<MutableList<Track>>{
        mutableListOf()
    }
}