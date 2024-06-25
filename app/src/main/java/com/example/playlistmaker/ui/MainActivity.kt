package com.example.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.MusicPlayer
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b_search = findViewById<View>(R.id.button_search)
        val b_play_list = findViewById<View>(R.id.button_play_list)
        val b_settings = findViewById<View>(R.id.button_settings)

        b_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        b_play_list.setOnClickListener {
            val intent = Intent(this, PlayListActivity::class.java)
            startActivity(intent)
        }

        b_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        MusicPlayer.destroy()
    }
}