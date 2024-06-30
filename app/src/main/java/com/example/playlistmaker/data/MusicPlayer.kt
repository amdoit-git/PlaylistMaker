package com.example.playlistmaker.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.Track

class MusicPlayer {

    companion object {
        private var mediaPlayer: MediaPlayer? = null
        private var onComplete: (() -> Unit)? = null
        private var track: Track? = null

        fun startPlayOrStop(track: Track): Boolean {

            return if (track.isPlaying) {
                destroy()
                false
            } else {
                play(track)
                true
            }
        }

        fun play(track: Track) {


            if (mediaPlayer != null) {
                destroy()
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA).build()
                )
                setDataSource(track.previewUrl)
                prepareAsync()

            }

            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }

            mediaPlayer?.setOnCompletionListener {
                onComplete?.let { func -> func() }
                destroy()
            }

            Companion.track = track
        }

        fun setOnCompleteCallback(onComplete: (() -> Unit)?) {
            Companion.onComplete = onComplete
        }

        fun destroy() {
            mediaPlayer?.release()
            mediaPlayer = null
            track = null
        }

        fun isPlayingNow(track: Track): Boolean {

            Companion.track?.let {
                return it.trackId == track.trackId
            }

            return false
        }
    }
}