package com.example.playlistmaker.viewModels.mediaLibrary.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FavoriteTracksTabViewModel(
    private val favorite: FavoriteTracksInteractor
) : ViewModel() {

    init {

        viewModelScope.launch(Dispatchers.Main) {

            favorite.getAllTracks().flowOn(Dispatchers.IO).collect { tracks ->

                liveData.setValue(FavoriteTabData.TrackList(tracks = tracks))

                if (tracks.isNotEmpty()) {
                    liveData.setSingleEventValue(FavoriteTabData.ScrollTracksList(0))
                }
            }
        }
    }

    private val liveData = LiveDataWithStartDataSet<FavoriteTabData>()

    private var trackClickAllowed = true

    fun getLiveData(): LiveData<FavoriteTabData> {
        return liveData
    }

    fun onTrackClicked(track: Track) {

        if (!isClickAllowed()) return

        liveData.setSingleEventValue(FavoriteTabData.OpenPlayerScreen(track))
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

        liveData.setValue(FavoriteTabData.TrackList(tracks = listOf()))
    }
}