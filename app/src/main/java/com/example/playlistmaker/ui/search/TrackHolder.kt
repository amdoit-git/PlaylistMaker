package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.databinding.TrackInListBinding

class TrackHolder(private val binding: TrackInListBinding) : SearchActivityHolder(binding.root) {

    override fun bind(track: Track, onTrackClick: (Track) -> Unit) {
        val url = if (track.isPlaying) R.drawable.playing else track.trackCover
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTime
        Glide.with(binding.trackCover).load(url).centerCrop()
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(dpToPx(2f, itemView.context))).into(binding.trackCover)

        itemView.setOnClickListener {
            onTrackClick(track)
        }
    }

    companion object {
        fun create(parent: ViewGroup): TrackHolder {

            return TrackHolder(
                TrackInListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}