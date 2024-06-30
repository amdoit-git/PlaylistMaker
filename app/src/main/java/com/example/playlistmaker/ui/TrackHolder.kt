package com.example.playlistmaker.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.MediaPlayerService
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.repository.TracksHistoryRepositoryImpl
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecase.TracksHistoryInteractorImpl

class TrackHolder(view: View) : SearchActivityHolder(view) {

    val trackName: TextView = itemView.findViewById(R.id.trackName)
    val artistName: TextView = itemView.findViewById(R.id.artistName)
    val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    val cover: ImageView = itemView.findViewById(R.id.trackCover)
    private val repository by lazy { TracksHistoryRepositoryImpl(view.context) }
    private val tracksHistory by lazy{ TracksHistoryInteractorImpl(repository=repository) }

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

        tracksHistory.save(track)

        updateTracks(this.adapterPosition)

        val intent = Intent(itemView.context, PlayerScreenActivity::class.java)

        intent.putExtra("track", SearchHistory.toJson(track));

        itemView.context.startActivity(intent)

        MediaPlayerService.setDataSource(track.previewUrl)
    }

    companion object {
        fun create(parent: ViewGroup): TrackHolder {
            return TrackHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.track_in_list, parent, false)
            )
        }
    }
}