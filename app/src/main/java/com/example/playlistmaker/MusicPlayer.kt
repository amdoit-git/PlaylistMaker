package com.example.playlistmaker

import android.media.AudioAttributes
import android.media.MediaPlayer

class MusicPlayer {

    companion object{
        private var mediaPlayer: MediaPlayer? = null;
        private var onComplete: (() -> Unit)? = null;

        fun play(url:String) {

            if (mediaPlayer != null) {
                destroy();
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepareAsync()
                //prepare() // might take long! (for buffering, etc)
                //start()
            }

            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start();
            }

            mediaPlayer?.setOnCompletionListener {
                onComplete?.let { func -> func() }
                destroy()
            }
        }

        fun setOnCompleteCallback(onComplete: (()->Unit)?){
            this.onComplete = onComplete
        }

        fun destroy() {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}