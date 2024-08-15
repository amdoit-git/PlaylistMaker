package com.example.playlistmaker.di

import com.example.playlistmaker.viewModels.main.MainViewModel
import com.example.playlistmaker.viewModels.player.PlayerScreenViewModel
import com.example.playlistmaker.viewModels.search.SearchViewModel
import com.example.playlistmaker.viewModels.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(context = get(), history = get(), iTunes = get())
    }

    viewModel { (jsonTrack: String) ->
        PlayerScreenViewModel(context = get(), player = get(), history = get(), jsonTrack)
    }

    viewModel {
        SettingsViewModel(navigator = get(), settings = get())
    }

    viewModel {
        MainViewModel(context = androidContext())
    }
}