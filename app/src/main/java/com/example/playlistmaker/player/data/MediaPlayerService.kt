package com.example.playlistmaker.player.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock

class MediaPlayerService {

    companion object {

        enum class State(val num: Int) {

            STOPPED(0), PLAYING(1), PAUSED(2);

            companion object {
                fun find(num: Int): State {
                    return State.entries.firstOrNull { it.num == num } ?: STOPPED
                }
            }
        }

        private var mediaPlayer: MediaPlayer = MediaPlayer()
        private var isPrepared = false
        private var isError = false
        private var onPLay: ((Int) -> Unit)? = null
        private var onDuration: ((Int) -> Unit)? = null
        private var onStop: (() -> Unit)? = null
        private var onError: (() -> Unit)? = null
        private var url: String? = null

        private var STATE = State.STOPPED
        private val handler = Handler(Looper.getMainLooper())
        private val obj = Any()

        fun setDisplayPorts(
            forTime: ((Int) -> Unit)?,
            forDuration: ((Int) -> Unit)?,
            forStop: (() -> Unit)?,
            forError: (() -> Unit)?
        ) {

            onPLay = forTime
            onDuration = forDuration
            onStop = forStop
            onError = forError


            if (isPrepared) {
                displayPlayProgress()
            }
        }

        fun resetDisplayPorts(){
            setDisplayPorts(null,null,null,null)
        }

        fun setDataSource(url: String) {

            Companion.url = url;
            mediaPlayer.reset()
            isPrepared = false
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()

            )
            mediaPlayer.setDataSource(url)

            mediaPlayer.setOnPreparedListener {
                isPrepared = true

                if (STATE == State.PLAYING) {
                    play()
                }
                displayPlayProgress()
            }
            mediaPlayer.setOnCompletionListener {
                stop()
            }
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                isError = true
                if (STATE == State.PLAYING) {
                    onError?.let { it() }
                }
                return@setOnErrorListener false
            }
            mediaPlayer.prepareAsync()
        }

        fun play(): Boolean {
            if (isError) {
                onError?.let { it() }
                return false
            }
            if (isPrepared) {
                mediaPlayer.start()
                setTimeout()
            }
            STATE = State.PLAYING
            return true
        }

        fun pause() {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                displayPlayProgress()
            }
            STATE = State.PAUSED
            handler.removeCallbacksAndMessages(obj)
            onStop?.let { it() }
        }

        fun stop() {
            if (isPrepared) {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.seekTo(0)
                displayPlayProgress()
            }
            STATE = State.STOPPED
            handler.removeCallbacksAndMessages(obj)
            onStop?.let { it() }
        }

        fun destroy() {
            handler.removeCallbacksAndMessages(obj)
            mediaPlayer.release()
            url = null
        }

        private fun setTimeout() {
            handler.removeCallbacksAndMessages(obj)
            handler.postAtTime({
                displayPlayProgress()
                setTimeout()
            }, obj,1000L + SystemClock.uptimeMillis())
        }


        fun displayPlayProgress() {
            if (isPrepared) {
                onPLay?.let { it(mediaPlayer.currentPosition) }
                onDuration?.let { it(mediaPlayer.duration) }
            }
        }

        fun getPosition(): Int {
            return if (isPrepared) mediaPlayer.currentPosition else 0
        }

        fun setPosition(currentPosition: Int) {
            if (isPrepared) {

                if (currentPosition < mediaPlayer.duration) {
                    mediaPlayer.seekTo(currentPosition)
                } else {
                    mediaPlayer.seekTo(mediaPlayer.duration)
                }
            }
        }
    }
}