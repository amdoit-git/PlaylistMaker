package com.example.playlistmaker

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.testapp.Track

class TrackAdapter(var tracks: MutableList<Track>) : RecyclerView.Adapter<TrackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(parent);
    }

    override fun getItemCount(): Int {
        return tracks.size;
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position]);

        setOnTrackClick(holder);//play music on click
    }

    private fun setOnTrackClick(holder: TrackHolder){
        holder.itemView.setOnClickListener {
            val track = tracks[holder.adapterPosition];

            if(track.isPlaying){
                track.isPlaying = false;
                MusicPlayer.destroy()
            }
            else {
                track.isPlaying = true;
                MusicPlayer.play(track.previewUrl)
            }

            updateTracks(holder.adapterPosition);
        }
    }

    fun updateTracks(index:Int = -1){
        for(i in 0 until tracks.size){
            if(i==index){
                this.notifyItemChanged(i)
            }
            else {
                val track = tracks.get(i)
                if (track.isPlaying) {
                    track.isPlaying = false;
                    this.notifyItemChanged(i)
                }
            }
        }
    }
}