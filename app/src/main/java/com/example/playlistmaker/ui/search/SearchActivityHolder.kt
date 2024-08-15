package com.example.playlistmaker.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.domain.models.Track

open class SearchActivityHolder(view: View) : RecyclerView.ViewHolder(view), DpToPx {

    open fun bind(track: Track, onTrackClick: (Track) -> Unit) {}

    open fun onButtonClick(callback: () -> Unit) {}
}
