package com.example.playlistmaker.ui.mediaLibrary.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.mediaLibrary.MediaLibraryFragmentDirections
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.viewModels.mediaLibrary.favorite.FavoriteTabData
import com.example.playlistmaker.viewModels.mediaLibrary.favorite.FavoriteTracksTabViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteTracksTabFragment : Fragment() {

    private lateinit var tracksList: RecyclerView

    private val vModel: FavoriteTracksTabViewModel by viewModel()

    private val adapter: TrackAdapter by inject {
        parametersOf(vModel::onTrackClicked, vModel::clearHistory, ::scrollListToTop)
    }

    private var _binding: FragmentFavoriteTracksBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksList = binding.tracksList

        tracksList.adapter = adapter

        vModel.getLiveData().observe(viewLifecycleOwner) {

            when (it) {

                is FavoriteTabData.TrackList -> {

                    if (it.tracks.isNotEmpty()) {
                        binding.tracksList.isVisible = true
                        binding.noTracks.isVisible = false
                        showTracksList(it.tracks)
                    } else {
                        binding.tracksList.isVisible = false
                        binding.noTracks.isVisible = true
                        clearTracksList()
                    }
                }

                is FavoriteTabData.MoveToTop -> {
                    adapter.moveTrackToTop(it.track)
                }

                is FavoriteTabData.OpenPlayerScreen -> {

                    val direction =
                        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerScreenFragment(it.track)

                    findNavController().navigate(direction)
                }

                is FavoriteTabData.ScrollTracksList -> {
                    scrollTracksList(it.position)
                }
            }
        }
    }

    private fun scrollListToTop() {
        tracksList.scrollToPosition(0)
    }

    private fun showTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.setNewTracksList(tracks)

        adapter.showClearButton(showClearButton)

        adapter.notifyDataSetChanged()
    }

    private fun scrollTracksList(position: Int) {
        tracksList.smoothScrollToPosition(position)
    }

    private fun clearTracksList() {
        adapter.clearTracks()
        adapter.showClearButton(false)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = FavoriteTracksTabFragment()
    }
}