package com.example.playlistmaker

import android.content.Context
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

class TrackAdapter(private var tracks: MutableList<Track>) : RecyclerView.Adapter<TrackAdapter.TrackHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(parent);
    }

    override fun getItemCount(): Int {
        return tracks.size;
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position]);
    }

    class TrackHolder(view: View) : RecyclerView.ViewHolder(view) {

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.track_in_list, parent, false));

        val trackName: TextView = itemView.findViewById(R.id.trackName);
        val artistName: TextView = itemView.findViewById(R.id.artistName);
        val trackTime: TextView = itemView.findViewById(R.id.trackTime);
        val cover: ImageView = itemView.findViewById(R.id.trackCover);

        fun bind(track: Track) {
            trackName.text = track.trackName;
            artistName.text = track.artistName;
            trackTime.text = track.trackTime;
            Glide.with(cover)
                .load(track.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder)
                .transform(RoundedCorners(dpToPx(2f, itemView.context)))
                .into(cover);
        }

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
    }
}