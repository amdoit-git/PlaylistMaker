package com.example.playlistmaker.creator

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
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

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(
            repository = provideMediaPlayerRepository()
        )
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(
            mediaPlayer = MediaPlayer()
        )
    }

    fun provideTracksHistoryInteractor(context: Context): TrackHistoryInteractor {
        return TracksHistoryInteractorImpl(
            repository = provideTracksHistoryRepository(
                sharedPreferences = context.getSharedPreferences(
                    APP_SETTINGS_PREFERENCES, MODE_PRIVATE
                )
            )
        )
    }

    private fun provideTracksHistoryRepository(sharedPreferences: SharedPreferences): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(
            sharedPreferences = sharedPreferences
        )
    }

    fun provideITunesInteractor(context: Context): ITunesInteractor {
        return ITunesInteractorImpl(
            repository = provideITunesRepository(context)
        )
    }

    private fun provideITunesRepository(context: Context): ITunesRepository {
        return ITunesRepositoryImpl(context)
    }

    fun provideExternalNavigatorInteractor(context: Context): ExternalNavigatorInteractor {
        return ExternalNavigatorInteractorImpl(
            repository = provideExternalNavigatorRepository(
                context
            )
        )
    }

    private fun provideExternalNavigatorRepository(context: Context): ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl(context)
    }

    fun provideAppSettingsInteractor(context: Context): AppSettingsInteractor {
        return AppSettingsInteractorImpl(
            repository = provideAppSettingsRepository(context)
        )
    }

    private fun provideAppSettingsRepository(context: Context): AppSettingsRepository {
        return AppSettingsRepositoryImpl(
            sharedPreferences = context.getSharedPreferences(
                APP_SETTINGS_PREFERENCES,
                MODE_PRIVATE
            )
        )
    }
}