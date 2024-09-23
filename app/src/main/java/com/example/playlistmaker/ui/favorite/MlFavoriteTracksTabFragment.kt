package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.viewModels.favorite.MlFavoriteTracksTabViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MlFavoriteTracksTabFragment : Fragment() {

    private val vModel: MlFavoriteTracksTabViewModel by viewModel()

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

        vModel.log("MlFavoriteTracksTabFragment view created")


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