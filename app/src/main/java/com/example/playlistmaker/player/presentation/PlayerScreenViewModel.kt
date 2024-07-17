package com.example.playlistmaker.player.presentation

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerScreenViewModel(private val application: Application, jsonTrack: String) :
    ViewModel() {

    private val liveDataTrackProgress = MutableLiveData<TrackProgress>()
    private val liveDataTrackInfo = MutableLiveData<Track>()
    private val liveDataToast = MutableLiveData<ToastMessage>()
    private val trackProgress = TrackProgress()

    private val player = Creator.provideMediaPlayerInteractor()
    private val history = Creator.provideTrackHistoryInteractor()
    private val handlerToast = Handler(Looper.getMainLooper())
    private val obj = Any()

    private lateinit var track: Track

    init {

        history.jsonToTrack(jsonTrack)?.let {
            track = it
            liveDataTrackInfo.postValue(track)
            player.setDataSource(track.previewUrl)
            player.setDisplayPorts(::showPlayProgress, null, ::onPlayingStopped, ::onPlayerError)
        }
    }

    fun play() {
        player.play()
        trackProgress.stopped = false
        liveDataTrackProgress.postValue(TrackProgress(stopped = false))
    }

    fun pause() {
        player.pause()
        trackProgress.stopped = true
        liveDataTrackProgress.postValue(TrackProgress(stopped = true))
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
        onPlayingStopped()
        showToast(getString(R.string.player_screen_track_error))
    }

    fun showToast(message: String, seconds: Int = 5) {

        Log.d("toast", "showToast(message: String, seconds: Int = 5)")

        handlerToast.removeCallbacksAndMessages(obj)

        handlerToast.postAtTime({
            liveDataToast.postValue(ToastMessage(message = "", isVisible = false))
        }, obj, seconds * 1000L + SystemClock.uptimeMillis())

        liveDataToast.postValue(ToastMessage(message = message, isVisible = true))
    }

    private fun displayProgress() {
        liveDataTrackProgress.postValue(trackProgress)
    }

    private fun getString(id: Int): String {
        return application.applicationContext.resources.getString(id)
    }

    fun getProgressLiveData(): LiveData<TrackProgress> {
        return liveDataTrackProgress
    }

    fun getTrackInfoLiveData(): LiveData<Track> {
        return liveDataTrackInfo
    }

    fun getToastLiveData(): LiveData<ToastMessage> {
        return liveDataToast
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