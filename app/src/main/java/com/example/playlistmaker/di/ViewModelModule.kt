package com.example.playlistmaker.di

import com.example.playlistmaker.viewModels.mediaLibrary.MediaLibraryViewModel
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.AddNewPlayListViewModel
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlayListsTabViewModel
import com.example.playlistmaker.viewModels.mediaLibrary.favorite.FavoriteTracksTabViewModel
import com.example.playlistmaker.viewModels.main.MainActivityViewModel
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
            strings = get(),
            player = get(),
            favorite = get(),
            notice = get(),
            playlists = get(),
            history = get(),
            jsonTrack
        )
    }

    viewModel {
        SettingsViewModel(navigator = get(), settings = get())
    }

    viewModel {
        MediaLibraryViewModel()
    }

    viewModel {
        FavoriteTracksTabViewModel(favorite = get(), history = get())
    }

    viewModel {
        PlayListsTabViewModel(playlists = get())
    }

    viewModel {
        AddNewPlayListViewModel(playlists = get(), notice = get(), strings = get())
    }

    viewModel {
        MainActivityViewModel(notice = get())
    }
}