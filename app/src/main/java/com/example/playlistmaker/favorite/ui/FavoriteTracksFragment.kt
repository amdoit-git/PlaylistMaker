package com.example.playlistmaker.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.favorite.viewModel.FavoriteTracksViewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel: FavoriteTracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite_tracks, container, false)
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}