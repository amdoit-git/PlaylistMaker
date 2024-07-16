package com.example.playlistmaker

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.player.domain.models.Track

class TrackAdapter(
    val tracks: MutableList<Track> = mutableListOf()
) : RecyclerView.Adapter<SearchActivityHolder>() {

    var hasClearButton: Boolean = false
    private var onButtonClick: (() -> Unit)? = null
    private var scrollMe: ((Int) -> Unit)? = null
    private var clickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
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
            TYPE_TRACK -> holder.bind(tracks[position], ::updateTracks, ::isClickAllowed)
            TYPE_BUTTON -> holder.onButtonClick(onButtonClick)
            else -> throw IllegalArgumentException("Неизвестный тип SearchActivityHolder!!!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < tracks.size) TYPE_TRACK else TYPE_BUTTON
    }

    fun showClearButton(onClick: (() -> Unit)?, scrollMe: ((Int) -> Unit)? = null) {
        if (onClick == null) {
            onButtonClick = null
            hasClearButton = false
        } else {
            onButtonClick = onClick
            hasClearButton = true
        }
        this.scrollMe = scrollMe
    }

    companion object {
        private const val TYPE_TRACK = 0
        private const val TYPE_BUTTON = 1
    }

    private fun isClickAllowed(): Boolean {
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({
                clickAllowed = true
            }, 1000)
            return true
        }
        return false
    }

    fun updateTracks(index: Int = -1) {

        for (i in 0 until tracks.size) {
            if (i == index) {
                this.notifyItemChanged(i)
            } else {
                val track = tracks.get(i)
                if (track.isPlaying) {
                    track.isPlaying = false
                    this.notifyItemChanged(i)
                }
            }
        }

        if (hasClearButton && index > 0) {

            tracks.add(0, tracks.removeAt(index))

            scrollMe?.let {
                it(0)
            }

            this.notifyItemMoved(index, 0)
        }
    }
}