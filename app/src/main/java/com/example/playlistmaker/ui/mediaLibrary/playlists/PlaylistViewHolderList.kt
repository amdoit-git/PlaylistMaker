package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlaylistInListBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistViewHolderList(private val binding: PlaylistInListBinding) : PlaylistViewHolder(binding.root) {

    override fun bind(playlist: Playlist, onClick: (Playlist) -> Unit, trackCounterDeclination: String) {

        binding.title.text = playlist.title
        binding.tracksTotal.text = declension(playlist.tracksTotal, trackCounterDeclination)
        playlist.coverUri?.let { uri ->
            Glide.with(binding.cover).load(uri).transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, binding.cover.context))
            ).into(binding.cover)
        } ?: run {
            Glide.with(binding.cover).clear(binding.cover);
        }

        itemView.setOnClickListener {
            onClick(playlist)
        }
    }


    companion object {
        fun create(parent: ViewGroup): PlaylistViewHolderList {
            return PlaylistViewHolderList(
                PlaylistInListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}