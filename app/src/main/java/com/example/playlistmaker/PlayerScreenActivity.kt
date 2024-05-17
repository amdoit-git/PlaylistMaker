package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerScreenActivity : AppCompatActivity(), DpToPX {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_screen)

        intent.getStringExtra("track")?.let { json ->

            SearchHistory.jsonToTrack(json)?.let {
                findViewById<TextView>(R.id.trackName).text = it.trackName;
                findViewById<TextView>(R.id.artistName).text = it.artistName;
                findViewById<TextView>(R.id.durationValue).text = it.trackTime;
                findViewById<TextView>(R.id.albumValue).text = it.albumName;
                findViewById<TextView>(R.id.yearValue).text = it.albumYear;
                findViewById<TextView>(R.id.genreValue).text = it.genre;
                findViewById<TextView>(R.id.countryValue).text = it.country;

                val cover = findViewById<ImageView>(R.id.albumCover);

                val coverUrl = it.trackCover.replaceAfterLast('/', "512x512bb.jpg");

                Glide.with(cover).load(coverUrl).centerCrop()
                    .placeholder(R.drawable.track_placeholder)
                    .transform(RoundedCorners(dpToPx(8f, cover.context))).into(cover)
            }
        }

        findViewById<View>(R.id.backButton).setOnClickListener {
            this.finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}