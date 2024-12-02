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
import com.example.playlistmaker.ui.search.TrackAdapterData
import com.example.playlistmaker.viewModels.mediaLibrary.favorite.FavoriteTabData
import com.example.playlistmaker.viewModels.mediaLibrary.favorite.FavoriteTracksTabViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksTabFragment : Fragment() {

    private lateinit var tracksList: RecyclerView

    private lateinit var adapter: TrackAdapter

    private val vModel: FavoriteTracksTabViewModel by viewModel()

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

        adapter = TrackAdapter()

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
                        MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerScreenFragment(
                            it.track.trackId
                        )

                    findNavController().navigate(direction)
                }

                is FavoriteTabData.ScrollTracksList -> {
                    scrollTracksList(it.position)
                }
            }
        }

        adapter.getLiveData().observe(viewLifecycleOwner) {

            when (it) {
                is TrackAdapterData.ButtonClick -> {
                    vModel.clearHistory()
                }

                is TrackAdapterData.ScrollTracksList -> {
                    scrollTracksList(it.position)
                }

                is TrackAdapterData.TrackClick -> {
                    vModel.onTrackClicked(it.track)
                }

                is TrackAdapterData.TrackLongClick -> {}
            }
        }
    }

    private fun showTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.setNewTracksList(tracks, showClearButton)
    }

    private fun scrollTracksList(position: Int) {
        tracksList.smoothScrollToPosition(position)
    }

    private fun clearTracksList() {
        adapter.clearTracks()
    }

    companion object {
        fun newInstance() = FavoriteTracksTabFragment()
    }
}