package com.example.playlistmaker.viewModels.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.favorite.tracks.FavoriteTracksInteractor
import com.example.playlistmaker.domain.repository.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerScreenViewModel(
    private val context: Context,
    private val player: MediaPlayerInteractor,
    private val favorite: FavoriteTracksInteractor,
    private val notice: NoticeInteractor,
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

                favorite.findTrackIds(track.trackId).flowOn(Dispatchers.IO).collect { ids ->

                    track.isFavorite = ids.isNotEmpty()

                    liveData.setValue(PlayerScreenData.FavoriteStatus(isFavorite = ids.isNotEmpty()))
                }
            }

            player.setDataSource(track.previewUrl)
            player.setDisplayPorts(::showPlayProgress, null, ::onPlayingStopped, ::onPlayerError)
            liveData.setValue(PlayerScreenData.TrackData(track = track))
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
                context.resources.getString(R.string.player_screen_track_error)
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

    override fun onCleared() {
        super.onCleared()
        player.resetDisplayPorts()
        handlerToast.removeCallbacksAndMessages(obj)
    }
}