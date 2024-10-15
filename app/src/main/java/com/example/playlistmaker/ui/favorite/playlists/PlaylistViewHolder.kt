package com.example.playlistmaker.ui.favorite.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlaylistInGridBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.ui.common.NumDeclension

class PlaylistViewHolder(private val binding: PlaylistInGridBinding) :
    RecyclerView.ViewHolder(binding.root), DpToPx, NumDeclension {

    fun bind(playlist: Playlist, onClick: (Playlist) -> Unit, trackCounterDeclination: String) {

        binding.title.text = playlist.title
        binding.tracksTotal.text = declension(playlist.tracksTotal, trackCounterDeclination)
        playlist.coverUri?.let { uri ->
            Glide.with(binding.cover).load(uri).transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, binding.cover.context))
            ).into(binding.cover)
        }

        itemView.setOnClickListener {
            onClick(playlist)
        }
    }


    companion object {
        fun create(parent: ViewGroup): PlaylistViewHolder {
            return PlaylistViewHolder(
                PlaylistInGridBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}