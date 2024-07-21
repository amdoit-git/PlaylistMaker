package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.presentation.MainViewModel
import com.example.playlistmaker.common.presentation.SCREEN_NAME

class MainActivity : AppCompatActivity() {

    private lateinit var presenter: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        presenter =
            ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java]

        binding.buttonSearch.setOnClickListener {
            presenter.openScreen(SCREEN_NAME.SEARCH)
        }

        binding.buttonPlayList.setOnClickListener {
            presenter.openScreen(SCREEN_NAME.PLAYLIST)
        }

        binding.buttonSettings.setOnClickListener {
            presenter.openScreen(SCREEN_NAME.SETTINGS)
        }
    }
}