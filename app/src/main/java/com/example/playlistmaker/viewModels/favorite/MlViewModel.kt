package com.example.playlistmaker.viewModels.favorite

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor

class MlViewModel(
    private val history: TracksHistoryInteractor,
    private val favorite: FavoriteTracksInteractor
) : ViewModel() {
}