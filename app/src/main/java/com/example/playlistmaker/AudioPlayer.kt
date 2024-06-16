package com.example.playlistmaker

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException

enum class PLAYER_BUTTON(val num: Int) {

    STOPPED(0), PLAYING(1), PAUSED(2);

    companion object {
        fun find(num: Int): PLAYER_BUTTON {
            return PLAYER_BUTTON.entries.firstOrNull { it.num == num } ?: STOPPED
        }
    }
}

class AudioPlayer(val onProgress: (Int, Int) -> Unit, val onStopped: () -> Unit, val onError:()->Unit) {

    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var isPrepared = false
    private var savedPosition = 0
    private var STATE = PLAYER_BUTTON.STOPPED

    fun setDataSource(url: String) {

        mediaPlayer.reset()

        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build()

        )
        mediaPlayer.setDataSource(url)

        mediaPlayer.setOnPreparedListener {
            isPrepared = true

            if (savedPosition > 0 && savedPosition < mediaPlayer.duration) {
                mediaPlayer.seekTo(savedPosition)
            }
            if (STATE == PLAYER_BUTTON.PLAYING) {
                play()
            }
            showProgress()
        }
        mediaPlayer.setOnCompletionListener {
            stop()
        }
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            onError();
            return@setOnErrorListener false;
        }
        mediaPlayer.prepareAsync()
    }

    fun play() {
        if (isPrepared) {
            mediaPlayer.start()
            showProgressDelayed()
        }
        STATE = PLAYER_BUTTON.PLAYING
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            showProgress()
        }
        STATE = PLAYER_BUTTON.PAUSED
        handler.removeCallbacksAndMessages(null)
        onStopped()
    }

    fun stop() {
        if(isPrepared) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.seekTo(0)
            showProgress()
        }
        STATE = PLAYER_BUTTON.STOPPED
        handler.removeCallbacksAndMessages(null)
        onStopped()
    }

    fun getPosition(): Int {
        return if (isPrepared) mediaPlayer.currentPosition else 0
    }

    fun setPosition(currentPosition: Int) {
        savedPosition = currentPosition
    }

    private fun showProgressDelayed() {
        handler.postDelayed({
            showProgress()
            showProgressDelayed()
        }, 1000)
    }

    private fun showProgress() {
        if (isPrepared) {
            onProgress(mediaPlayer.currentPosition, mediaPlayer.duration)
        }
    }

    fun destroy() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
}