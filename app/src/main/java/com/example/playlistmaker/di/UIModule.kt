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

    factory<ActivityPlayerScreenBinding> { (layoutInflater: LayoutInflater) ->
        ActivityPlayerScreenBinding.inflate(layoutInflater)
    }

    factory<ActivitySettingsBinding> { (layoutInflater: LayoutInflater) ->
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    factory<ActivitySearchBinding> { (layoutInflater: LayoutInflater) ->
        ActivitySearchBinding.inflate(layoutInflater)
    }

    factory<ActivityMainBinding> { (layoutInflater: LayoutInflater) ->
        ActivityMainBinding.inflate(layoutInflater)
    }

    factory<TrackAdapter> { (onTrackClick: (Track) -> Unit, onButtonClick: () -> Unit, scrollListToTop: () -> Unit) ->
        TrackAdapter(onTrackClick, onButtonClick, scrollListToTop, get())
    }

    factory<MutableList<Track>> {
        mutableListOf()
    }
}