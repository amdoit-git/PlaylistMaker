package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ButtonHolder(view: View) : SearchActivityHolder(view) {

    override fun onButtonClick(callback: (() -> Unit)?) {
        callback?.let {
            itemView.findViewById<Button>(R.id.clear_search_histroy_button).setOnClickListener {
                callback()
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup): ButtonHolder {
            return ButtonHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.clear_search_history_button, parent, false)
            )
        }
    }
}