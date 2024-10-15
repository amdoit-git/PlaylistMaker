package com.example.playlistmaker.ui.favorite.playlists

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Playlist

class PlaylistAdapter(
    private val playlists: MutableList<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val scrollListToTop: () -> Unit,
    private val trackCounterDeclination: String
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], onPlaylistClick, trackCounterDeclination)
    }

    fun setNewPlaylists(playlists: List<Playlist>) {
        this.playlists.clear()
        playlists.forEach {
            this.playlists.add(it)
        }
    }

    fun clearPlaylists() {
        this.playlists.clear()
    }
}