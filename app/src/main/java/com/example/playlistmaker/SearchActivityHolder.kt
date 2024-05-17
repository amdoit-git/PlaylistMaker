package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.Track

interface DpToPX {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}

open class SearchActivityHolder(view: View) : RecyclerView.ViewHolder(view), DpToPX {

    open fun bind(track: Track, updateTracks: (Int) -> Unit) {}

    open fun onButtonClick(callback: (() -> Unit)?) {}

}
