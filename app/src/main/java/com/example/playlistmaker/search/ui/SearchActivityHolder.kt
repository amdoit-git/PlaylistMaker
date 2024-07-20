package com.example.playlistmaker.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.ui.DpToPx
import com.example.playlistmaker.common.domain.models.Track

open class SearchActivityHolder(view: View) : RecyclerView.ViewHolder(view), DpToPx {

    open fun bind(track: Track, updateTracks: (Int) -> Unit, isClickAllowed:()->Boolean) {}

    open fun onButtonClick(callback: (() -> Unit)?) {}

}
