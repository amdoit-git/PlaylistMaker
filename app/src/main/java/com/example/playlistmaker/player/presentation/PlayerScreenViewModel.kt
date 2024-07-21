package com.example.playlistmaker.player.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.presentation.LiveDataWithStartDataSet
import com.example.playlistmaker.creator.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerScreenViewModel(private val application: Application, jsonTrack: String) :
    ViewModel() {

    private val liveData = LiveDataWithStartDataSet<PlayerScreenData>()
    private val trackProgress = PlayerScreenData.TrackProgress()

    private val player = Creator.provideMediaPlayerInteractor()
    private val history = Creator.provideTracksHistoryInteractor()
    private val handlerToast = Handler(Looper.getMainLooper())
    private val obj = Any()

    private lateinit var track: Track

    init {

        history.jsonToTrack(jsonTrack)?.let {
            track = it
            liveData.setValue(PlayerScreenData.TrackData(track = track))
            player.setDataSource(track.previewUrl)
            player.setDisplayPorts(::showPlayProgress, null, ::onPlayingStopped, ::onPlayerError)
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

    fun showToast(message: String, seconds: Int = 3) {

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
        return application.applicationContext.resources.getString(id)
    }

    fun getLiveData(): LiveData<PlayerScreenData> {
        return liveData
    }

    fun addToFavorite(){
        showToast(String.format(
            getString(R.string.player_screen_add_to_favorite), track.trackName
        ))
    }

    fun removeFromFavorite(){
        showToast(String.format(
            getString(R.string.player_screen_remove_from_favorite), track.trackName
        ))
    }

    override fun onCleared() {
        super.onCleared()
        player.resetDisplayPorts()
        handlerToast.removeCallbacksAndMessages(obj)
    }

    class Factory(private val application: Application, private val json: String) :
        ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlayerScreenViewModel(application, json) as T
        }
    }
}