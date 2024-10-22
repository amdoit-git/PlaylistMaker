package com.example.playlistmaker.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.common.DpToPx

open class SearchActivityHolder(view: View) : RecyclerView.ViewHolder(view), DpToPx {

    open fun bind(track: Track, onClick: (Track) -> Unit, onLongClick: ((Track) -> Unit)?) {}

    open fun bindButton(buttonText: String, onClick: () -> Unit) {}
}
