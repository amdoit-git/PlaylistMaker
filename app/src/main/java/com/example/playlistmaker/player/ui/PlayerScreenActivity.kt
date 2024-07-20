package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.ui.DpToPx
import com.example.playlistmaker.databinding.ActivityPlayerScreenBinding
import com.example.playlistmaker.player.presentation.PlayerScreenData
import com.example.playlistmaker.player.presentation.PlayerScreenViewModel

class PlayerScreenActivity : AppCompatActivity(), DpToPx {

    data class TrackData(val keyId: Int, val valueId: Int, val value: String)

    private lateinit var presenter: PlayerScreenViewModel
    private lateinit var binding: ActivityPlayerScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityPlayerScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        intent.getStringExtra("track")?.let { json ->

            presenter = ViewModelProvider(
                this,
                PlayerScreenViewModel.Factory(application, json)
            )[PlayerScreenViewModel::class.java]

            presenter.getLiveData().observe(this) {

                when (it) {

                    is PlayerScreenData.TrackData -> fillTrackInfo(it.track)

                    is PlayerScreenData.TrackProgress -> {
                        it.time?.let { time ->
                            binding.playTime.text = time
                        }

                        it.duration?.let {

                        }

                        it.stopped?.let { stopped ->
                            binding.playPauseBt.isChecked = !stopped
                        }
                    }

                    is PlayerScreenData.ToastMessage -> {

                        binding.infoText.text = it.message

                        binding.info.visibility = if (it.isVisible) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
            }

            binding.playPauseBt.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    presenter.play()
                } else {
                    presenter.pause()
                }
            }

            binding.backButton.setOnClickListener {
                this.finish()
            }

            binding.favoriteBt.setOnCheckedChangeListener { button, isChecked ->

                if (button.isPressed) {

                    presenter.showToast(
                        if (isChecked) {
                            String.format(
                                getString(R.string.player_screen_add_to_favorite), "track.trackName"
                            )
                        } else {
                            String.format(
                                getString(R.string.player_screen_remove_from_favorite),
                                "track.trackName"
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) {
            presenter.pause()
        }
    }

    private fun fillTrackInfo(track: Track) {

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName

        val cover = binding.albumCover

        val coverUrl = track.trackCover.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(cover).load(coverUrl).centerCrop().placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(dpToPx(8f, cover.context))).into(cover)


        val constraintLayout = binding.scrollBody

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

            if (item.value.isBlank() || item.value == "-") {

                //скрываем пустые поля в свойствах трека и отвязываем их от соседей
                constraintSet.clear(item.valueId)
                constraintSet.clear(item.keyId)
                constraintSet.setVisibility(item.valueId, View.GONE)
                constraintSet.setVisibility(item.keyId, View.GONE)
            } else {

                //соединяем непустые поля к другим непустым полям
                constraintSet.connect(
                    item.keyId, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM
                )

                //выставляем значения непустых полей
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