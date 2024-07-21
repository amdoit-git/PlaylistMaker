package com.example.playlistmaker.creator

import android.content.Context
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

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    fun provideTracksHistoryInteractor(): TrackHistoryInteractor {
        return TracksHistoryInteractorImpl(provideTracksHistoryRepository())
    }

    fun provideTracksHistoryRepository(): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl()
    }

    fun provideITunesInteractor(context: Context): ITunesInteractor {
        return ITunesInteractorImpl(provideITunesRepository(context))
    }

    fun provideITunesRepository(context: Context): ITunesRepository {
        return ITunesRepositoryImpl(context)
    }

    fun provideExternalNavigatorInteractor(context: Context): ExternalNavigatorInteractor {
        return ExternalNavigatorInteractorImpl(provideExternalNavigatorRepository(context))
    }

    fun provideExternalNavigatorRepository(context: Context): ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl(context)
    }

    fun provideAppSettingsInteractor(context: Context): AppSettingsInteractor {
        return AppSettingsInteractorImpl(provideAppSettingsRepository(context))
    }

    fun provideAppSettingsRepository(context: Context): AppSettingsRepository {
        return AppSettingsRepositoryImpl(context)
    }
}