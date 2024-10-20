package com.example.playlistmaker.viewModels.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.common.GetStringResourceUseCase
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.mediaLibrary.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.repository.mediaLibrary.favorite.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerScreenViewModel(
    private val strings: GetStringResourceUseCase,
    private val player: MediaPlayerInteractor,
    private val favorite: FavoriteTracksInteractor,
    private val notice: NoticeInteractor,
    private val playlists: PlaylistsInteractor,
    history: TracksHistoryInteractor,
    jsonTrack: String
) :
    ViewModel() {

    private val liveData = LiveDataWithStartDataSet<PlayerScreenData>()
    private val trackProgress = PlayerScreenData.TrackProgress()

    private val handlerToast = Handler(Looper.getMainLooper())
    private val obj = Any()

    private lateinit var track: Track

    init {

        history.jsonToTrack(jsonTrack)?.let {

            track = it

            viewModelScope.launch(Dispatchers.Main) {

                favorite.containsTrack(track.trackId).flowOn(Dispatchers.IO).collect { count ->

                    track.isFavorite = count>0

                    liveData.setValue(PlayerScreenData.FavoriteStatus(isFavorite = track.isFavorite))
                }
            }

            player.setDataSource(track.previewUrl)
            player.setDisplayPorts(::showPlayProgress, null, ::onPlayingStopped, ::onPlayerError)
            liveData.setValue(PlayerScreenData.TrackData(track = track))
        }

        viewModelScope.launch(Dispatchers.Main) {

            playlists.getPlaylists().flowOn(Dispatchers.IO).collect { list ->

                liveData.setValue(
                    if (list.isNotEmpty()) {
                        PlayerScreenData.Playlists(playlists = list)
                    } else {
                        PlayerScreenData.PlaylistNotFound(message = "")
                    }
                )
            }
        }
    }

    fun play() {
        trackProgress.stopped = false
        player.play()
        displayProgress()
    }

    fun pause() {
        trackProgress.stopped = true
        player.pause()
        displayProgress()
    }

    private fun showPlayProgress(currentTime: Int) {
        trackProgress.time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime)
        displayProgress()
    }

    private fun onPlayingStopped() {
        trackProgress.stopped = true
        displayProgress()
    }

    private fun onPlayerError() {
        trackProgress.stopped = true
        displayProgress()

        viewModelScope.launch {
            notice.setMessage(
                strings(R.string.player_screen_track_error)
            )
        }
    }

    private fun displayProgress() {
        liveData.setValue(trackProgress)
    }

    fun getLiveData(): LiveData<PlayerScreenData> {
        return liveData
    }

    fun addToFavorite() {

        viewModelScope.launch(Dispatchers.IO) {
            favorite.saveTrack(track)
        }

        liveData.setStartValue(PlayerScreenData.FavoriteStatus(isFavorite = true))
    }

    fun removeFromFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favorite.deleteTrack(track.trackId)
        }

        liveData.setStartValue(PlayerScreenData.FavoriteStatus(isFavorite = false))
    }

    fun onPlaylistClick(playlist: Playlist) {
        viewModelScope.launch {

            if (playlists.containsTrack(
                    playlistId = playlist.id,
                    trackId = track.trackId
                )
            ) {
                notice.setMessage(
                    strings(R.string.play_list_fail_add_track_tpl).replace(
                        "[playlist]",
                        playlist.title
                    )
                )
            } else {

                playlists.addTrack(
                    track = track, playlistId = playlist.id
                )

                setBottomSheetState(false)

                notice.setMessage(
                    strings(R.string.play_list_success_add_track_tpl).replace(
                        "[playlist]",
                        playlist.title
                    )
                )
            }
        }
    }

    fun setBottomSheetState(opened: Boolean) {
        liveData.setValue(PlayerScreenData.BottomSheet(opened))
    }

    override fun onCleared() {
        super.onCleared()
        player.resetDisplayPorts()
        handlerToast.removeCallbacksAndMessages(obj)
    }
}