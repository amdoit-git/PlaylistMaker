package com.example.playlistmaker.viewModels.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.viewModels.common.ScreenName

class MainViewModel(private val context: Context) : ViewModel() {

    fun openScreen(screen: ScreenName) {

        val intent = Intent(context, screen.className)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }
}