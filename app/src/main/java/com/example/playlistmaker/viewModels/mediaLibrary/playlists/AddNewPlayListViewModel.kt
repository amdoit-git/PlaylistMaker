package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.common.GetStringResourceUseCase
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.mediaLibrary.playlists.PlaylistsInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddNewPlayListViewModel(
    private val playlists: PlaylistsInteractor,
    private val notice: NoticeInteractor,
    private val strings: GetStringResourceUseCase
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

    fun saveCoverToTmpStorage(bitmap: Bitmap, uri: Uri) {

        liveData.postValue(NewPlaylistTabData.Cover(uri = uri))

        coverUri = uri

        bitmapEncodeJob = viewModelScope.launch(Dispatchers.Main) {

            coverUri = playlists.saveCoverToTmpDir(bitmap)

            liveData.postValue(NewPlaylistTabData.Cover(uri = coverUri))
        }
    }

    fun showMessage(message: String) {

        viewModelScope.launch {
            notice.setMessage(text = message)
        }
    }

    fun showMessage(id: Int) {

       showMessage(message = strings(id))
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

    fun onCreateButtonPressed() {

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

            notice.setMessage(strings(R.string.play_list_created_tpl).replace("[playlist]", title))
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