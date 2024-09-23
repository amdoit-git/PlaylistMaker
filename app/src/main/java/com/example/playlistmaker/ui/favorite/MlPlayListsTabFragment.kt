package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentPlayListBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.viewModels.favorite.FavoriteData
import com.example.playlistmaker.viewModels.favorite.MlPlayListsTabViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MlPlayListsTabFragment : Fragment() {

    private lateinit var tracksList: RecyclerView

    private val vModel: MlPlayListsTabViewModel by viewModel()

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

                is FavoriteData.TrackList -> {


                }

                is FavoriteData.MoveToTop -> {
                    adapter.moveTrackToTop(it.track)
                }

                is FavoriteData.OpenPlayerScreen -> {

                    val direction =
                        MlFragmentDirections.actionMediaLibraryFragmentToPlayerScreenFragment(it.track)

                    findNavController().navigate(direction)
                }

                is FavoriteData.ScrollTracksList -> {
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
        fun newInstance() = MlPlayListsTabFragment()
    }
}