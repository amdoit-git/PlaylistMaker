package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import android.view.LayoutInflater
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
import com.example.playlistmaker.common.domain.repository.TracksHistoryInteractor
import com.example.playlistmaker.common.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.databinding.ActivityPlayerScreenBinding
import com.example.playlistmaker.main.presentation.MainViewModel
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.player.presentation.PlayerScreenViewModel
import com.example.playlistmaker.search.data.Itunes
import com.example.playlistmaker.search.data.impl.ITunesRepositoryImpl
import com.example.playlistmaker.search.domain.impl.ITunesInteractorImpl
import com.example.playlistmaker.search.domain.repository.ITunesInteractor
import com.example.playlistmaker.search.domain.repository.ITunesRepository
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(mediaPlayer = get())
    }

    factory<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(sharedPreferences = get(), gson = get())
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
