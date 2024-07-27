package com.example.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.common.presentation.ScreenName
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.presentation.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val vModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonSearch.setOnClickListener {
            vModel.openScreen(ScreenName.SEARCH)
        }

        binding.buttonPlayList.setOnClickListener {
            vModel.openScreen(ScreenName.PLAYLIST)
        }

        binding.buttonSettings.setOnClickListener {
            vModel.openScreen(ScreenName.SETTINGS)
        }
    }
}