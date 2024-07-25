package com.example.playlistmaker.main.presentation

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.common.presentation.ScreenName

class MainViewModel(private val context: Context) : ViewModel() {

    fun openScreen(screen: ScreenName) {

        val intent = Intent(context, screen.className)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

//    class Factory(private val application: Application) :
//        ViewModelProvider.AndroidViewModelFactory(application) {
//
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return MainViewModel(application) as T
//        }
//    }
}