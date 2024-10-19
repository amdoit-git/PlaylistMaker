package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.repository.mediaLibrary.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(private val playlists: PlaylistsInteractor, val playlistId: Int) :
    ViewModel() {

    private val liveData = LiveDataWithStartDataSet<PlaylistScreenData>()

    init {

        viewModelScope.launch {

            playlists.getPlaylistInfo(playlistId).collect { playlist ->

                liveData.setValue(PlaylistScreenData.Info(playlist = playlist))
            }

            playlists.getTracks(playlistId).collect { list ->

                liveData.setValue(PlaylistScreenData.Tracks(tracks = list))
            }
        }
    }

    fun getLiveData(): LiveData<PlaylistScreenData> {
        return liveData
    }
}