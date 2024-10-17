package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.ui.common.NumDeclension

sealed class PlaylistViewHolder(view: View) :
    RecyclerView.ViewHolder(view), DpToPx, NumDeclension {

    open fun bind(
        playlist: Playlist,
        onClick: (Playlist) -> Unit,
        trackCounterDeclination: String
    ) {}
}