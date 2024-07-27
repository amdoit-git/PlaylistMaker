package com.example.playlistmaker.di

import android.view.LayoutInflater
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.databinding.ActivityPlayerScreenBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.search.ui.TrackAdapter
import org.koin.dsl.module

val uiModule = module {

    factory<TrackAdapter> { (onTrackClick: (Track) -> Unit, onButtonClick: () -> Unit, scrollListToTop: () -> Unit) ->
        TrackAdapter(onTrackClick, onButtonClick, scrollListToTop, tracks = mutableListOf())
    }
}