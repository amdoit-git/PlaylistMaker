package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsGridBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.mediaLibrary.MediaLibraryFragmentDirections
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlayListsTabViewModel
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlaylistsTabData
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsTabFragment : Fragment() {

    private val vModel: PlayListsTabViewModel by viewModel()

    private var _binding: FragmentPlaylistsGridBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val info = binding.noPlaylists

        vModel.getLiveData().observe(viewLifecycleOwner) {

            when (it) {

                is PlaylistsTabData.Playlists -> {

                    if (it.playlists.isNotEmpty()) {
                        recyclerView.isVisible = true
                        info.isVisible = false
                        adapter.setNewPlaylists(it.playlists)
                    } else {
                        recyclerView.isVisible = false
                        info.isVisible = true
                    }
                }
            }
        }

        recyclerView = binding.recyclerView

        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        adapter = PlaylistAdapter(
            playlists = mutableListOf(),
            onPlaylistClick = ::onPlaylistClick,
            scrollListToTop = ::scrollListToTop,
            trackCounterDeclination = getString(R.string.track_counter_declination),
            type = PlaylistRvType.GRID
        )

        recyclerView.adapter = adapter

        binding.btAddNewPlaylist.setOnClickListener {
            val direction =
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToAddNewPlayListFragment()

            findNavController().navigate(direction)
        }
    }

    private fun scrollListToTop(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    private fun onPlaylistClick(playlist: Playlist) {

        val direction =
            MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistScreenFragment(
                playlistId = playlist.id
            )

        findNavController().navigate(direction)
    }

    companion object {
        fun newInstance() = PlayListsTabFragment()
    }
}