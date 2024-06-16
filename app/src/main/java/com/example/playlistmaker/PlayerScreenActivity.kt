package com.example.playlistmaker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.testapp.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackData(val keyId: Int, val valueId: Int, val value: String)

class PlayerScreenActivity : AppCompatActivity(), DpToPx {

    private lateinit var player: AudioPlayer
    private lateinit var playPauseBt: ToggleButton
    private lateinit var playTime: TextView
    private lateinit var track: Track
    private val TRACK_PROGRESS_TIME = "TRACK_PROGRESS_TIME"
    private val handlerToast = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_screen)

        player = AudioPlayer(::showPlayProgress, ::onPlayingStopped, ::onPlayerError)

        savedInstanceState?.let {
            player.setPosition(it.getInt(TRACK_PROGRESS_TIME, 0))
        }

        intent.getStringExtra("track")?.let { json ->

            SearchHistory.jsonToTrack(json)?.let {

                track = it

                fillTrackInfo()

                initPlayer()
            }
        }

        findViewById<View>(R.id.backButton).setOnClickListener {
            this.finish()
        }

        findViewById<ToggleButton>(R.id.favoriteBt).setOnCheckedChangeListener { _, isChecked ->
            showToast(
                if (isChecked) {
                    String.format(
                        getString(R.string.player_screen_add_to_favorite),
                        track.trackName
                    )
                } else {
                    String.format(
                        getString(R.string.player_screen_remove_from_favorite),
                        track.trackName
                    )
                }
            )
        }
    }

    private fun initPlayer() {

        playTime = findViewById(R.id.playTime)

        playPauseBt = findViewById(R.id.playPauseBt)

        playPauseBt.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                player.play()
            } else {
                player.pause()
            }
        }

        player.setDataSource(track.previewUrl + "kdkdk")
    }

    private fun showPlayProgress(currentTime: Int, duration: Int) {

        playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime)
    }

    private fun onPlayingStopped() {
        playPauseBt.isChecked = false
    }

    private fun onPlayerError() {
        showPlayProgress(0, 0);
        showToast(getString(R.string.player_screen_track_error), 10)
    }

    private fun showToast(message: String, seconds: Int = 2) {

        val info = findViewById<View>(R.id.info)
        val infoText = findViewById<TextView>(R.id.infoText);
        infoText.text = message
        info.visibility = View.VISIBLE
        handlerToast.postDelayed({
            info.visibility = View.GONE
        }, seconds * 1000L)
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TRACK_PROGRESS_TIME, player.getPosition())
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
        handlerToast.removeCallbacksAndMessages(null)
    }

    private fun fillTrackInfo() {

        findViewById<TextView>(R.id.trackName).text = track.trackName
        findViewById<TextView>(R.id.artistName).text = track.artistName

        val cover = findViewById<ImageView>(R.id.albumCover)

        val coverUrl = track.trackCover.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(cover).load(coverUrl).centerCrop().placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(dpToPx(8f, cover.context))).into(cover)


        val constraintLayout = findViewById<ConstraintLayout>(R.id.scrollBody)

        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)

        val list = listOf(
            TrackData(R.id.duration, R.id.durationValue, track.trackTime),
            TrackData(R.id.album, R.id.albumValue, track.albumName),
            TrackData(R.id.year, R.id.yearValue, track.albumYear),
            TrackData(R.id.genre, R.id.genreValue, track.genre),
            TrackData(R.id.country, R.id.countryValue, track.country)
        )

        var topId: Int = R.id.trackDataStart

        for (item in list) {

            if (item.value.isEmpty() || item.value == "-") {
                constraintSet.clear(item.valueId)
                constraintSet.clear(item.keyId)
                constraintSet.setVisibility(item.valueId, View.GONE)
                constraintSet.setVisibility(item.keyId, View.GONE)
            } else {
                constraintSet.connect(
                    item.keyId, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM
                )
                findViewById<TextView>(item.valueId).text = item.value
                topId = item.keyId
            }
        }

        constraintSet.connect(
            R.id.scrollBottom, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(constraintLayout)
    }
}