package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerScreenBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.ui.favorite.playlists.PlaylistAdapter
import com.example.playlistmaker.ui.favorite.playlists.PlaylistRvType
import com.example.playlistmaker.viewModels.player.PlayerScreenData
import com.example.playlistmaker.viewModels.player.PlayerScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerScreenFragment : Fragment(), DpToPx {

    data class TrackData(val keyId: Int, val valueId: Int, val value: String)

    private lateinit var json: String

    private val vModel: PlayerScreenViewModel by viewModel {
        parametersOf(json)
    }

    private var _binding: ActivityPlayerScreenBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityPlayerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                binding.overlay.isVisible = if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    vModel.setBottomSheetState(opened = false)

                    false
                } else {
                    true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (1 + slideOffset) / 2
            }
        })

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)

        arguments?.let { args ->

            args.getString(TRACK)?.let { json ->

                this.json = json

                vModel.getLiveData().observe(viewLifecycleOwner) {

                    when (it) {

                        is PlayerScreenData.TrackData -> fillTrackInfo(it.track)

                        is PlayerScreenData.TrackProgress -> {
                            it.time?.let { time ->
                                binding.playTime.text = time
                            }

                            it.stopped?.let { stopped ->
                                binding.playPauseBt.isChecked = !stopped
                            }
                        }

                        is PlayerScreenData.FavoriteStatus -> {
                            binding.favoriteBt.isChecked = it.isFavorite
                        }

                        is PlayerScreenData.Playlists -> {
                            adapter.setNewPlaylists(it.playlists)

                            adapter.notifyDataSetChanged()
                        }

                        is PlayerScreenData.PlaylistNotFound -> {}

                        is PlayerScreenData.BottomSheet -> {

                            if (it.opened) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                                binding.overlay.isVisible = true
                                binding.overlay.alpha = 0.5f
                            } else {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
                                binding.overlay.isVisible = false
                            }
                        }
                    }
                }

                recyclerView = binding.recyclerView

                adapter = PlaylistAdapter(
                    playlists = mutableListOf(),
                    onPlaylistClick = ::onPlaylistClick,
                    scrollListToTop = ::scrollListToTop,
                    trackCounterDeclination = getString(R.string.track_counter_declination),
                    type = PlaylistRvType.LIST
                )

                recyclerView.adapter = adapter

                binding.playPauseBt.setOnCheckedChangeListener { _, isChecked ->

                    if (isChecked) {
                        vModel.play()
                    } else {
                        vModel.pause()
                    }
                }

                binding.backButton.setOnClickListener {
                    findNavController().popBackStack()
                }

                binding.favoriteBt.setOnCheckedChangeListener { button, isChecked ->

                    if (button.isPressed) {

                        if (isChecked) {
                            vModel.addToFavorite()
                        } else {
                            vModel.removeFromFavorite()
                        }
                    }
                }
            }
        }

        binding.playlistBt.setOnClickListener {
            vModel.setBottomSheetState(opened = true)
        }

        binding.btAddNewPlaylist.setOnClickListener {
            val direction =
                PlayerScreenFragmentDirections.actionPlayerScreenFragmentToAddNewPlayListFragment()

            findNavController().navigate(direction)
        }

        binding.overlay.setOnClickListener {
            vModel.setBottomSheetState(opened = false)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.let {
            if (!it.isChangingConfigurations) {
                binding.playPauseBt.isChecked = false
            }
        }
    }

    private fun fillTrackInfo(track: Track) {

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName

        val cover = binding.albumCover

        val coverUrl = track.trackCover.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(cover).load(coverUrl).transform(
            CenterCrop(), RoundedCorners(dpToPx(8f, cover.context))
        ).placeholder(R.drawable.track_placeholder).into(cover)


        val constraintLayout = binding.scrollBody

        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)

        val list = listOf(
            TrackData(R.id.duration, R.id.durationValue, track.trackTime),
            TrackData(R.id.album, R.id.albumValue, track.albumName),
            TrackData(R.id.year, R.id.yearValue, track.albumYear),
            TrackData(R.id.genre, R.id.genreValue, track.genre),
            TrackData(R.id.country, R.id.countryValue, track.country)
        )

        var topId: Int = R.id.trackDataStart

        for (item in list) {

            if (item.value.isBlank() || item.value == "-") {

                //скрываем пустые поля в свойствах трека и отвязываем их от соседей
                constraintSet.clear(item.valueId)
                constraintSet.clear(item.keyId)
                constraintSet.setVisibility(item.valueId, View.GONE)
                constraintSet.setVisibility(item.keyId, View.GONE)
            } else {

                //соединяем непустые поля к другим непустым полям
                constraintSet.connect(
                    item.keyId, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM
                )

                //выставляем значения непустых полей
                view?.let {
                    it.findViewById<TextView>(item.valueId).text = item.value
                }

                topId = item.keyId
            }
        }

        constraintSet.connect(
            R.id.scrollBottom, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(constraintLayout)
    }

    private fun scrollListToTop() {
        recyclerView.scrollToPosition(0)
    }

    private fun onPlaylistClick(playlist: Playlist) {

        vModel.onPlaylistClick(
            playlist = playlist
        )
    }

    companion object {
        const val TRACK = "track"
    }
}