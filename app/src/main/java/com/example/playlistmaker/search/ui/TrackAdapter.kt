package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TrackAdapter(
    private val onTrackClick: (Track) -> Unit,
    private val onButtonClick: () -> Unit,
    val tracks: MutableList<Track> = mutableListOf()
) : RecyclerView.Adapter<SearchActivityHolder>() {

    private var hasClearButton: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchActivityHolder {
        return when (viewType) {
            TYPE_TRACK -> TrackHolder.create(parent)
            TYPE_BUTTON -> ButtonHolder.create(parent)
            else -> throw IllegalArgumentException("Неизвестный тип SearchActivityHolder!!!")
        }
    }

    override fun getItemCount(): Int {
        return tracks.size + if (hasClearButton) 1 else 0
    }

    override fun onBindViewHolder(holder: SearchActivityHolder, position: Int) {

        when (getItemViewType(position)) {
            TYPE_TRACK -> holder.bind(tracks[position], onTrackClick)
            TYPE_BUTTON -> holder.onButtonClick(onButtonClick)
            else -> throw IllegalArgumentException("Неизвестный тип SearchActivityHolder!!!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < tracks.size) TYPE_TRACK else TYPE_BUTTON
    }

    fun showClearButton(visible: Boolean) {
        hasClearButton = visible
    }

    companion object {
        private const val TYPE_TRACK = 0
        private const val TYPE_BUTTON = 1
    }
}