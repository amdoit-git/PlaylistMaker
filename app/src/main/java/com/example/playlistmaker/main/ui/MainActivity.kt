package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.presentation.MainViewModel
import com.example.playlistmaker.common.presentation.ScreenName

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, MainViewModel.Factory(application))[MainViewModel::class.java]

        binding.buttonSearch.setOnClickListener {
            viewModel.openScreen(ScreenName.SEARCH)
        }

        binding.buttonPlayList.setOnClickListener {
            viewModel.openScreen(ScreenName.PLAYLIST)
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.openScreen(ScreenName.SETTINGS)
        }
    }
}