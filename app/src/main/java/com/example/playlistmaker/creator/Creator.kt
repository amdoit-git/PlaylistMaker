package com.example.playlistmaker.creator

import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.TracksHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.MediaPlayerInteractor
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.repository.TrackHistoryInteractor
import com.example.playlistmaker.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.domain.usecase.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.usecase.TracksHistoryInteractorImpl

object Creator {

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(provideMediaPlayerRepository())
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository{
        return MediaPlayerRepositoryImpl()
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TracksHistoryInteractorImpl(provideTracksHistoryRepository())
    }

    fun provideTracksHistoryRepository(): TracksHistoryRepository{
        return TracksHistoryRepositoryImpl()
    }
}