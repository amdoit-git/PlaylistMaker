package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.common.NumDeclension
import com.example.playlistmaker.ui.mediaLibrary.MediaLibraryFragmentDirections
import com.example.playlistmaker.ui.search.SearchFragmentDirections
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.TrackAdapterData
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlaylistScreenData
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlaylistScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.round

class PlaylistScreenFragment() : Fragment(), NumDeclension {

    private val vModel: PlaylistScreenViewModel by viewModel {
        parametersOf(playlistId)
    }

    private var playlistId = 0

    private var _binding: FragmentPlaylistScreenBinding? = null

    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksList: RecyclerView
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistTracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        arguments?.let { args ->

            args.getInt(PLAYLIST_ID).let { playlistId ->

                this.playlistId = playlistId

                vModel.getLiveData().observe(viewLifecycleOwner) {

                    when (it) {
                        is PlaylistScreenData.Info -> {
                            fillPlaylistInfo(it.playlist)
                            setTracksBSPeekHeight()
                        }

                        is PlaylistScreenData.Tracks -> {

                            adapter.setNewTracksList(it.tracks)

                            adapter.showClearButton(true)

                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }

        setTracksBSPeekHeight()

        adapter = TrackAdapter("Очистить плейлист", longClickEnabled = true)

        tracksList = binding.recyclerView

        tracksList.adapter = adapter

        adapter.getLiveData().observe(viewLifecycleOwner) {

            when(it){
                is TrackAdapterData.ButtonClick -> {
                    //vModel.clearHistory()
                }
                is TrackAdapterData.ScrollTracksList -> {
                    //scrollTracksList(it.position)
                }
                is TrackAdapterData.TrackClick -> {
                    //vModel.onTrackClicked(it.track)

                    val direction =
                        PlaylistScreenFragmentDirections.actionPlaylistScreenFragmentToPlayerScreenFragment(it.track.trackId)

                    findNavController().navigate(direction)
                }
                is TrackAdapterData.TrackLongClick -> {}
            }
        }
    }

    private fun fillPlaylistInfo(playlist: Playlist) {

        with(playlist) {
            binding.title.text = title
            binding.description.text = description
            binding.description.isVisible = description.isNotBlank()
            binding.totalTime.text = declension(
                round(duration / 60f).toInt(),
                getString(R.string.minutes_counter_declination)
            )
            binding.totalTracks.text =
                declension(tracksTotal, getString(R.string.track_counter_declination))

            coverUri?.let { uri ->

                Glide.with(binding.cover).load(uri).into(binding.cover)
            }
        }
    }

    private fun setTracksBSPeekHeight() {

        binding.playlistTracksBottomSheet.isVisible = true

        binding.root.post {
            bottomSheetBehavior.peekHeight =
                Resources.getSystem().displayMetrics.heightPixels - getYPosition(binding.playlistInfoContainer) - binding.playlistInfoContainer.height
        }
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    companion object {
        const val PLAYLIST_ID = "playlistId"
    }
}