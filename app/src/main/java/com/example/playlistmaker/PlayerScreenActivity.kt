package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

data class TrackData(val keyId:Int, val valueId: Int, val value: String);

class PlayerScreenActivity : AppCompatActivity(), DpToPx {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_screen)

        intent.getStringExtra("track")?.let { json ->

            SearchHistory.jsonToTrack(json)?.let {
                findViewById<TextView>(R.id.trackName).text = it.trackName;
                findViewById<TextView>(R.id.artistName).text = it.artistName;

                val cover = findViewById<ImageView>(R.id.albumCover);

                val coverUrl = it.trackCover.replaceAfterLast('/', "512x512bb.jpg");

                Glide.with(cover).load(coverUrl).centerCrop()
                    .placeholder(R.drawable.track_placeholder)
                    .transform(RoundedCorners(dpToPx(8f, cover.context))).into(cover)


                val constraintLayout = findViewById<ConstraintLayout>(R.id.scrollBody)

                val constraintSet = ConstraintSet();

                constraintSet.clone(constraintLayout);

                val list = listOf(
                    TrackData(R.id.duration, R.id.durationValue, it.trackTime),
                    TrackData(R.id.album, R.id.albumValue, it.albumName),
                    TrackData(R.id.year, R.id.yearValue, it.albumYear),
                    TrackData(R.id.genre, R.id.genreValue, it.genre),
                    TrackData(R.id.country, R.id.countryValue, it.country)
                )

                var topId:Int = R.id.trackDataStart

                for(item in list){

                    if(item.value.isEmpty() || item.value=="-"){
                        constraintSet.clear(item.valueId)
                        constraintSet.clear(item.keyId)
                        constraintSet.setVisibility(item.valueId, View.GONE)
                        constraintSet.setVisibility(item.keyId, View.GONE)
                    }
                    else{
                        constraintSet.connect(item.keyId, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM)
                        findViewById<TextView>(item.valueId).text = item.value;
                        topId = item.keyId;
                    }
                }

                constraintSet.connect(R.id.scrollBottom, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM)

                constraintSet.applyTo(constraintLayout);
            }
        }

        findViewById<View>(R.id.backButton).setOnClickListener {
            this.finish()
        }

        findViewById<ToggleButton>(R.id.favoriteBt).setOnCheckedChangeListener { _, isChecked ->

            findViewById<View>(R.id.info).visibility = if (isChecked) View.VISIBLE else View.GONE;
        }
    }
}