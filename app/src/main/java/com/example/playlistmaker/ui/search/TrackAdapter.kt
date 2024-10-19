package com.example.playlistmaker.ui.search

import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.viewModels.common.SingleEventLiveData

class TrackAdapter(private val buttonText: String = "", private val longClickEnabled: Boolean = false) :
    RecyclerView.Adapter<SearchActivityHolder>() {

    private val tracks = mutableListOf<Track>()
    private var hasClearButton: Boolean = false
    private val liveData = SingleEventLiveData<TrackAdapterData>()
    private var clickedTrack: Track? = null

    fun getLiveData(): LiveData<TrackAdapterData> {
        return liveData
    }

    private fun onTrackClick(track: Track) {

        clickedTrack = track

        liveData.setValue(TrackAdapterData.TrackClick(track))
    }

    private fun onTrackLongClick(track: Track) {

        clickedTrack = track

        liveData.setValue(TrackAdapterData.TrackLongClick(track))
    }

    private fun onButtonClick() {

        //tracks.containsAll()

        liveData.setValue(TrackAdapterData.ButtonClick(true))
    }

    fun setNewTracksList(tracks: List<Track>) {
        this.tracks.clear()
        tracks.forEach {
            this.tracks.add(it)
        }
    }


//    private fun compare(old:List<Track>, new:List<Track>){
//
//        val newPos = mutableListOf<Int?>()
//
//        for(i in 0 until old.size){
//
//            val trackId = old[i].trackId
//
//            for(j in 0 until new.size){
//
//                if(new[j].trackId==trackId){
//                    newPos.add(j)
//                    break
//                }
//            }
//
//            if(newPos.size<i+1){
//                newPos.add(null)
//            }
//        }
//
//        if(){
//
//        }
//    }

    fun clearTracks() {
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