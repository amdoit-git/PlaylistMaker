package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TrackAdapter(
    private val onTrackClick: (Track) -> Unit,
    private val onButtonClick: () -> Unit,
    private val scrollListToTop: () -> Unit,
    private val tracks: MutableList<Track>
) : RecyclerView.Adapter<SearchActivityHolder>() {

    private var hasClearButton: Boolean = false

    fun setNewTracksList(tracks: List<Track>) {
        this.tracks.clear()
        tracks.forEach {
            this.tracks.add(it)
        }
    }

    fun clearTracks(){
        this.tracks.clear()
    }

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

    fun moveTrackToTop(track: Track) {

        var index = 0

        tracks.forEachIndexed { i, item ->
            if (item.trackId == track.trackId) {
                index = i
            }
        }

        if (index > 0) {
            tracks.add(0, tracks.removeAt(index))

            scrollListToTop()

            this.notifyItemMoved(index, 0)
        }
    }

    companion object {
        private const val TYPE_TRACK = 0
        private const val TYPE_BUTTON = 1
    }
}