package com.example.playlistmaker.di

import com.example.playlistmaker.domain.impl.favorite.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.impl.settings.AppSettingsInteractorImpl
import com.example.playlistmaker.domain.impl.settings.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.domain.impl.search.TracksHistoryInteractorImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.settings.AppSettingsInteractor
import com.example.playlistmaker.domain.repository.settings.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.domain.impl.player.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.repository.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.impl.search.ITunesInteractorImpl
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.search.ITunesInteractor
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

    factory<FavoriteTracksInteractor>{
        FavoriteTracksInteractorImpl(repository = get())
    }
}