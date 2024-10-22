package com.example.playlistmaker.ui.search

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.viewModels.common.SingleEventLiveData
import kotlin.math.abs

class TrackAdapter(
    private val buttonText: String = "",
    private val longClickEnabled: Boolean = false
) :
    RecyclerView.Adapter<SearchActivityHolder>() {

    private val tracks = mutableListOf<Track>()
    private var hasClearButton: Boolean = false
    private val liveData = SingleEventLiveData<TrackAdapterData>()

    fun getLiveData(): LiveData<TrackAdapterData> {
        return liveData
    }

    private fun onTrackClick(track: Track) {

        liveData.setValue(TrackAdapterData.TrackClick(track))
    }

    private fun onTrackLongClick(track: Track) {

        liveData.setValue(TrackAdapterData.TrackLongClick(track))
    }

    private fun onButtonClick() {

        liveData.setValue(TrackAdapterData.ButtonClick(true))
    }

    fun setNewTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        if (ifItemMoved(
                old = this.tracks,
                new = tracks
            )
        ) return

        if (ifItemDeleted(
                old = this.tracks,
                new = tracks
            )
        ) return

        if (ifItemAdded(
                old = this.tracks,
                new = tracks
            )
        ) return


        this.tracks.clear()

        tracks.forEach {
            this.tracks.add(it)
        }

        hasClearButton = showClearButton

        this.notifyDataSetChanged()
    }

    private fun ifItemAdded(old: MutableList<Track>, new: List<Track>): Boolean {

        //добавление трека

        if (new.size == old.size + 1 && new.containsAll(old)) {

            val t: Track = new.minus(old)[0]

            val toIndex = new.indexOf(t)

            val copy = old.toMutableList()

            copy.add(toIndex, t)

            if (copy == new) {

                old.add(toIndex, t)

                this.notifyItemInserted(toIndex)

                liveData.setValue(TrackAdapterData.ScrollTracksList(toIndex))

                return true
            }
        }

        return false
    }

    private fun ifItemDeleted(old: MutableList<Track>, new: List<Track>): Boolean {

        //удаление трека

        if (old.size == new.size + 1 && old.containsAll(new)) {

            val t: Track = old.minus(new)[0]

            val fromIndex = old.indexOf(t)

            val copy = old.toMutableList()

            copy.removeAt(fromIndex)

            if (copy == new) {

                old.removeAt(fromIndex)

                this.notifyItemRemoved(fromIndex)

                return true
            }
        }

        return false
    }

    private fun ifItemMoved(old: MutableList<Track>, new: List<Track>): Boolean {

        //Перемещение трека

        getMovedTrack(old, new)?.let { t ->

            val copy = old.toMutableList()

            val toIndex = new.indexOf(t)

            val fromIndex = old.indexOf(t)

            copy.add(toIndex, copy.removeAt(fromIndex))

            if (copy == new) {

                old.add(toIndex, old.removeAt(fromIndex))

                if (toIndex < fromIndex) {
                    liveData.setValue(TrackAdapterData.ScrollTracksList(toIndex))
                }

                this.notifyItemMoved(fromIndex, toIndex)

                return true
            }
        }

        return false
    }


    private fun getMovedTrack(old: List<Track>, new: List<Track>): Track? {

        if (old.size != new.size || !old.containsAll(new)) {
            return null
        }

        var maxMove = 0
        var maxMoveIndex = -1

        for (i in old.indices) {

            val trackId = old[i].trackId

            for (j in new.indices) {

                if (new[j].trackId == trackId) {
                    if (abs(i - j) > maxMove) {
                        maxMove = abs(i - j)
                        maxMoveIndex = i
                    }
                    break
                }
            }
        }

        return if (maxMove > 0) {
            old[maxMoveIndex]
        } else {
            null
        }
    }

    fun clearTracks() {
        this.tracks.clear()
        hasClearButton = false
        this.notifyDataSetChanged()
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

            TYPE_TRACK -> holder.bind(
                tracks[position],
                onClick = ::onTrackClick,
                onLongClick = if (longClickEnabled) ::onTrackLongClick else null
            )

            TYPE_BUTTON -> holder.bindButton(
                buttonText = buttonText,
                onClick = ::onButtonClick
            )

            else -> throw IllegalArgumentException("Неизвестный тип SearchActivityHolder!!!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < tracks.size) TYPE_TRACK else TYPE_BUTTON
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

            liveData.setValue(TrackAdapterData.ScrollTracksList(0))

            //this.notifyItemRemoved()

            this.notifyItemMoved(index, 0)
        }
    }

    companion object {
        private const val TYPE_TRACK = 0
        private const val TYPE_BUTTON = 1
    }
}