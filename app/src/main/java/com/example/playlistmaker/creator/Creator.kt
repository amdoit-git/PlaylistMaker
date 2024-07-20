package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.data.impl.TracksHistoryRepositoryImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.repository.TrackHistoryInteractor
import com.example.playlistmaker.player.domain.repository.TracksHistoryRepository
import com.example.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.data.impl.ITunesInteractorImpl
import com.example.playlistmaker.search.domain.repository.ITunesInteractor

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

    fun provideITunesInteractor(): ITunesInteractor {
        return ITunesInteractorImpl()
    }
}