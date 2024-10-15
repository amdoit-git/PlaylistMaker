package com.example.playlistmaker.viewModels.favorite.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlayListsTabViewModel(private val playlists: PlaylistsInteractor) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<PlaylistsTabData>()

    init {
        viewModelScope.launch(Dispatchers.Main) {

            playlists.getPlaylists().flowOn(Dispatchers.IO).collect { list ->

                liveData.setValue(
                    if (list.isNotEmpty()) {
                        PlaylistsTabData.Playlists(playlists = list)
                    } else {
                        PlaylistsTabData.Error(message = "")
                    }
                )
            }
        }
    }

    fun getLiveData(): LiveData<PlaylistsTabData> {
        return liveData
    }

    fun onPlaylistClick(playlist: Playlist) {

    }
}