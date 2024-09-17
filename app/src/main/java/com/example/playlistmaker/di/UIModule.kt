package com.example.playlistmaker.di

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.favorite.MlFragment
import com.example.playlistmaker.ui.search.TrackAdapter
import org.koin.dsl.module

val uiModule = module {

    factory<TrackAdapter> { (onTrackClick: (Track) -> Unit, onButtonClick: () -> Unit, scrollListToTop: () -> Unit) ->
        TrackAdapter(onTrackClick, onButtonClick, scrollListToTop, tracks = mutableListOf())
    }
}