package com.example.playlistmaker.viewModels.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddNewPlayListViewModel(private val playlists: PlaylistsInteractor) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<NewPlaylistData>()
    private var title = ""
    private var description = ""
    private var coverUri: Uri? = null
    private var playlistCreated = false

    fun getLiveData(): LiveData<NewPlaylistData> {
        return liveData
    }

    fun saveCoverToTmpStorage(bitmap: Bitmap?) {

        if (bitmap != null) {
            viewModelScope.launch(Dispatchers.Main) {

                coverUri = playlists.saveCoverToTmpDir(bitmap)

                liveData.postValue(NewPlaylistData.Cover(uri = coverUri))
            }
        } else {

            coverUri = null

            liveData.postValue(NewPlaylistData.Cover(uri = coverUri))
        }
    }

    fun onTitleChanged(text: String) {

        title = text

        liveData.setStartValue(NewPlaylistData.Title(text))

        liveData.postValue(
            NewPlaylistData.Button(
                enabled = text.isNotBlank()
            )
        )
    }

    fun onDescriptionChanged(text: String) {

        description = text

        liveData.setStartValue(NewPlaylistData.Description(text))
    }

    fun onCreateButtonPressed() {

        if(playlistCreated) return

        GlobalScope.launch(Dispatchers.IO) {

           playlists.addPlaylist(Playlist(
               id = 0,
               title = title,
               description = description,
               coverUri = coverUri
           ))
        }

        liveData.setSingleEventValue(
            NewPlaylistData.Close(allowed = true)
        )

        playlistCreated = true
    }

    fun onBackPressed() {
         liveData.setSingleEventValue(
            NewPlaylistData.Close(
                allowed = title.isBlank() && description.isBlank() && (coverUri == null)
            )
        )
    }
}