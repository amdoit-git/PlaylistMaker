package com.example.playlistmaker.viewModels.favorite.playlists

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.favorite.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddNewPlayListViewModel(
    private val playlists: PlaylistsInteractor,
    private val notice: NoticeInteractor
) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<NewPlaylistTabData>()
    private var title = ""
    private var description = ""
    private var coverUri: Uri? = null
    private var playlistCreated = false
    private var bitmapEncodeJob: Job? = null

    fun getLiveData(): LiveData<NewPlaylistTabData> {
        return liveData
    }

    fun saveCoverToTmpStorage(bitmap: Bitmap?, uri: Uri? = null) {

        coverUri = uri

        liveData.postValue(NewPlaylistTabData.Cover(uri = uri))

        if (bitmap != null) {

            bitmapEncodeJob = viewModelScope.launch(Dispatchers.Main) {

                coverUri = playlists.saveCoverToTmpDir(bitmap)

                liveData.postValue(NewPlaylistTabData.Cover(uri = coverUri))
            }
        }
    }

    fun onTitleChanged(text: String) {

        title = text

        liveData.setStartValue(NewPlaylistTabData.Title(text))

        liveData.postValue(
            NewPlaylistTabData.Button(
                enabled = text.isNotBlank()
            )
        )
    }

    fun onDescriptionChanged(text: String) {

        description = text

        liveData.setStartValue(NewPlaylistTabData.Description(text))
    }

    fun onCreateButtonPressed(tpl: String) {

        if (playlistCreated) return

        GlobalScope.launch(Dispatchers.IO) {

            //ждем сохранения обложки (если размер картинки гигантский)
            bitmapEncodeJob?.join()

            playlists.addPlaylist(
                Playlist(
                    id = 0,
                    title = title,
                    description = description,
                    coverUri = coverUri,
                    tracksTotal = 0
                )
            )

            notice.setMessage(tpl.replace("[playlist]", title))

            Log.d("WWW", coverUri.toString())
        }

        viewModelScope.launch(Dispatchers.Main) {

            bitmapEncodeJob?.join()

            liveData.setSingleEventValue(
                NewPlaylistTabData.Close(allowed = true)
            )
        }

        playlistCreated = true
    }

    fun onBackPressed() {
        liveData.setSingleEventValue(
            NewPlaylistTabData.Close(
                allowed = title.isBlank() && description.isBlank() && (coverUri == null)
            )
        )
    }
}