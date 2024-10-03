package com.example.playlistmaker.di

import com.example.playlistmaker.viewModels.favorite.MlFavoriteTracksTabViewModel
import com.example.playlistmaker.viewModels.favorite.MlPlayListsTabViewModel
import com.example.playlistmaker.viewModels.favorite.MlViewModel
import com.example.playlistmaker.viewModels.player.PlayerScreenViewModel
import com.example.playlistmaker.viewModels.search.SearchViewModel
import com.example.playlistmaker.viewModels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(history = get(), iTunes = get())
    }

    viewModel { (jsonTrack: String) ->
        PlayerScreenViewModel(
            context = get(),
            player = get(),
            history = get(),
            favorite = get(),
            jsonTrack
        )
    }

    viewModel {
        SettingsViewModel(navigator = get(), settings = get())
    }

    viewModel {
        MlViewModel()
    }

    viewModel {
        MlFavoriteTracksTabViewModel(favorite = get(), history = get())
    }

    viewModel {
        MlPlayListsTabViewModel()
    }
}