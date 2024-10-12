package com.example.playlistmaker.viewModels.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewPlayListViewModel(private val playlists: PlaylistsInteractor) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<NewPlaylistData>()
    private var title = ""
    private var description = ""
    private var cover: Uri? = null

    fun getLiveData(): LiveData<NewPlaylistData> {
        return liveData
    }

    fun saveCoverToTmpStorage(bitmap: Bitmap?) {

        if (bitmap != null) {
            viewModelScope.launch(Dispatchers.Main) {

                cover = Uri.parse(playlists.saveCoverToTmpDir(bitmap))

                liveData.postValue(NewPlaylistData.Cover(uri = cover))
            }
        } else {

            cover = null

            liveData.postValue(NewPlaylistData.Cover(uri = cover))
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


        liveData.setSingleEventValue(
            NewPlaylistData.Close(allowed = true)
        )
    }

    fun onBackPressed() {
        liveData.setSingleEventValue(
            NewPlaylistData.Close(
                allowed = title.isBlank() && description.isBlank() && (cover == null)
            )
        )
    }
}