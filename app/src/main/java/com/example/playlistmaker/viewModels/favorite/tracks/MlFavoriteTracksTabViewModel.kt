package com.example.playlistmaker.viewModels.favorite.tracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.favorite.tracks.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import com.example.playlistmaker.viewModels.favorite.FavoriteData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MlFavoriteTracksTabViewModel(
    private val favorite: FavoriteTracksInteractor,
    private val history: TracksHistoryInteractor
) : ViewModel() {

    init {

        viewModelScope.launch(Dispatchers.Main) {

            favorite.getAllTracks().flowOn(Dispatchers.IO).collect { tracks ->

                liveData.setValue(FavoriteData.TrackList(tracks = tracks))

                if (tracks.isNotEmpty()) {
                    liveData.setSingleEventValue(FavoriteData.ScrollTracksList(0))
                }
            }
        }
    }

    private val liveData = LiveDataWithStartDataSet<FavoriteData>()

    private var trackClickAllowed = true

    fun getLiveData(): LiveData<FavoriteData> {
        return liveData
    }

    fun onTrackClicked(track: Track) {

        if (!isClickAllowed()) return

        liveData.setSingleEventValue(FavoriteData.OpenPlayerScreen(history.toJson(track)))
    }

    private fun isClickAllowed(): Boolean {
        if (trackClickAllowed) {
            trackClickAllowed = false
            viewModelScope.launch(Dispatchers.Main) {
                delay(1000L)
                trackClickAllowed = true
            }
            return true
        }
        return false
    }

    fun clearHistory() {

        viewModelScope.launch(Dispatchers.Main) {
            favorite.clearTracks()
        }

        liveData.setValue(FavoriteData.TrackList(tracks = listOf()))
    }
}