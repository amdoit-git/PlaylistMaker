package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

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

        logMethodName(object {}.javaClass.enclosingMethod.name)
    }

    override fun onResume() {
        super.onResume()
        logMethodName(object {}.javaClass.enclosingMethod.name)
    }

    override fun onPause() {
        super.onPause()
        logMethodName(object {}.javaClass.enclosingMethod.name)
    }

    override fun onStart() {
        super.onStart()
        logMethodName(object {}.javaClass.enclosingMethod.name)
        MusicPlayer.destroy()
    }

    override fun onStop() {
        super.onStop()
        logMethodName(object {}.javaClass.enclosingMethod.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        logMethodName(object {}.javaClass.enclosingMethod.name)
    }

    private fun logMethodName(name: String) {
        //Log.d("TEST_TAG", "Мы в методе !!! " + name);
    }
}