package com.example.playlistmaker.viewModels.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
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
    history: TracksHistoryInteractor,
    private val favorite: FavoriteTracksInteractor,
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

            Log.d("WWW", "VM = " + Thread.currentThread().id)

            viewModelScope.launch(Dispatchers.Main) {

                Log.d("WWW", "Dispatchers.Main = " + Thread.currentThread().id)

                favorite.findTrackIds(track.trackId).flowOn(Dispatchers.IO).collect { ids ->

                    track.isFavorite = ids.isNotEmpty()

                    liveData.setValue(PlayerScreenData.FavoriteStatus(isFavorite = ids.isNotEmpty()))

                    Log.d("WWW", "collect = " + Thread.currentThread().id)
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
        showToast(getString(R.string.player_screen_track_error))
    }

    private fun showToast(message: String, seconds: Int = 3) {

        handlerToast.removeCallbacksAndMessages(obj)

        handlerToast.postAtTime({
            liveData.setValue(PlayerScreenData.ToastMessage(message = "", isVisible = false))
        }, obj, seconds * 1000L + SystemClock.uptimeMillis())

        liveData.setValue(PlayerScreenData.ToastMessage(message = message, isVisible = true))
    }

    private fun displayProgress() {
        liveData.setValue(trackProgress)
    }

    private fun getString(id: Int): String {
        return context.resources.getString(id)
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