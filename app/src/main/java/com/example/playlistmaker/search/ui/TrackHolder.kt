package com.example.playlistmaker.search.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.PlayerScreenActivity
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.data.SearchHistory

class TrackHolder(view: View) : SearchActivityHolder(view) {

    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val artistName: TextView = itemView.findViewById(R.id.artistName)
    val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    val cover: ImageView = itemView.findViewById(R.id.trackCover)

    override fun bind(track: Track, updateTracks: (Int) -> Unit, isClickAllowed:()->Boolean) {
        val url = if (track.isPlaying) R.drawable.playing else track.trackCover
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        Glide.with(cover).load(url).centerCrop().placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(dpToPx(2f, itemView.context))).into(cover)

        itemView.setOnClickListener {
            onTrackClick(track, updateTracks, isClickAllowed)
        }
    }

    private fun onTrackClick(track: Track, updateTracks: (Int) -> Unit, isClickAllowed:()->Boolean) {

        if(!isClickAllowed()) return Unit;

        //track.isPlaying = MusicPlayer.startPlayOrStop(track)
        //раскомментировать для проигрывания музыки

        SearchHistory.saveTrackInList(track)

        updateTracks(this.adapterPosition)

        val intent = Intent(itemView.context, PlayerScreenActivity::class.java)

        intent.putExtra("track", SearchHistory.toJson(track));

        itemView.context.startActivity(intent)
    }

    companion object {
        fun create(parent: ViewGroup): TrackHolder {
            return TrackHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.track_in_list, parent, false)
            )
        }
    }
}