package com.example.playlistmaker

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.testapp.Track

class MusicPlayer {

    companion object {
        private var mediaPlayer: MediaPlayer? = null;
        private var onComplete: (() -> Unit)? = null;
        private var track: Track? = null;

        fun startPlayOrStop(track: Track):Boolean{

            return if(track.isPlaying){
                destroy();
                false;
            } else{
                play(track);
                true;
            }
        }
        fun play(track: Track) {



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
                setDataSource(track.previewUrl)
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

            this.track = track;
        }

        fun setOnCompleteCallback(onComplete: (() -> Unit)?) {
            this.onComplete = onComplete
        }

        fun destroy() {
            mediaPlayer?.release()
            mediaPlayer = null
            track = null;
        }

        fun isPlayingNow(track: Track): Boolean {

            this.track?.let {
                return it.trackId == track.trackId;
            }

            return false;
        }
    }
}