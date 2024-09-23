package com.example.playlistmaker.viewModels.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.repository.favorite.FavoriteTracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MlFavoriteTracksTabViewModel(private val favorite: FavoriteTracksInteractor) : ViewModel() {

    init {

        viewModelScope.launch(Dispatchers.Main) {

           log("favorite.getAllTracks()")

            favorite.getAllTracks().flowOn(Dispatchers.IO).collect { tracks ->

                tracks.forEach {
                    log(it.toString())
                }
            }
        }
    }

    fun log(str:String){
        Log.d("WWW", str)
    }
}