package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.viewModels.favorite.MlFavoriteTracksTabViewModel
import org.koin.android.ext.android.inject

class MlFavoriteTracksTabFragment : Fragment() {

    private val viewModel: MlFavoriteTracksTabViewModel by viewModels()

    private val history: TracksHistoryInteractor by inject()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        lifecycleScope.launch(Dispatchers.Main) {
//            delay(3000L)
//
//            val track = history.getAll().get(0)
//
//            val direction = MlFragmentDirections.actionMediaLibraryFragmentToPlayerScreenFragment(history.toJson(track))
//
//            findNavController().navigate(direction)
//        }
    }

    companion object {
        fun newInstance() = MlFavoriteTracksTabFragment()
    }
}