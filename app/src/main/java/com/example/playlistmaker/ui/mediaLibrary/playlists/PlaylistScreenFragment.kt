package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.ui.common.NumDeclension
import com.example.playlistmaker.ui.common.LockableBottomSheetBehavior
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.TrackAdapterData
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlaylistScreenData
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.PlaylistScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs
import kotlin.math.round

class PlaylistScreenFragment() : Fragment(), NumDeclension {

    private val vModel: PlaylistScreenViewModel by viewModel {
        parametersOf(playlistId)
    }

    private var playlistId = 0

    private var _binding: FragmentPlaylistScreenBinding? = null

    private val binding get() = _binding!!

    private lateinit var tracksBS: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBS: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksList: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var playlist: Playlist

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

        arguments?.let { args ->

            args.getInt(PLAYLIST_ID).let { playlistId ->

                this.playlistId = playlistId

                vModel.getLiveData().observe(viewLifecycleOwner) {

                    when (it) {
                        is PlaylistScreenData.Info -> {
                            fillPlaylistInfo(it.playlist)
                            setTracksBSPeekHeight()
                            this.playlist = it.playlist
                        }

                        is PlaylistScreenData.Tracks -> {

                            if (it.tracks.isNotEmpty()) {
                                adapter.setNewTracksList(it.tracks)
                                binding.recyclerView.isVisible = true
                                binding.noTracks.isVisible = false
                            } else {
                                binding.recyclerView.isVisible = false
                                binding.noTracks.isVisible = true
                            }
                        }


                        is PlaylistScreenData.MenuBsState -> {

                            if (it.opened) {
                                menuBS.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                                binding.overlay.isVisible = true
                                binding.overlay.alpha = 0.5f
                            } else {
                                menuBS.setState(BottomSheetBehavior.STATE_HIDDEN)
                                binding.overlay.isVisible = false
                            }
                        }

                        is PlaylistScreenData.TracksBsState -> {

                            tracksBS.state = if (it.opened) {
                                BottomSheetBehavior.STATE_EXPANDED
                            } else {
                                BottomSheetBehavior.STATE_COLLAPSED
                            }
                        }

                        is PlaylistScreenData.GoBack -> goBack()
                    }
                }
            }
        }

        //инициализация элементов обложки плейлиста

        binding.backButton.setOnClickListener {
            goBack()
        }

        binding.sharePlaylistIcon.setOnClickListener {
            vModel.sharePlaylist()

        }

        binding.menuPlaylist.setOnClickListener {
            vModel.setMenuBsState(opened = true)
        }

        binding.overlay.setOnClickListener {
            vModel.setMenuBsState(opened = false)
        }

        binding.shareButton.setOnClickListener {
            vModel.sharePlaylist()

            vModel.setMenuBsState(opened = false)
        }

        binding.editButton.setOnClickListener {

            val direction =
                PlaylistScreenFragmentDirections.actionPlaylistScreenFragmentToAddNewPlayListFragment(
                    playlistId = playlistId
                )

            findNavController().navigate(direction)

            vModel.setMenuBsState(opened = false)
        }

        binding.deleteButton.setOnClickListener {
            confirm(
                title = getString(R.string.playlist_menu_delete_playlist),
                text = getString(R.string.playlist_menu_delete_playlist_question).replace(
                    "[playlist]",
                    playlist.title
                ),
                onConfirm = {
                    vModel.deletePlaylist()
                }
            )
        }

        binding.menuBottomSheet.setOnClickListener {
            //чтобы BS не пропускал клики в overlay и не закрывался когда не надо
        }

        binding.playlistTracksBottomSheet.setOnClickListener {
            //чтобы BS не пропускал клики по кнопкам поделиться и меню под ним
        }


        //инициализация списка треков в плейлисте

        adapter = TrackAdapter(getString(R.string.clear_playlist), longClickEnabled = true)

        tracksList = binding.recyclerView

        tracksList.adapter = adapter

        adapter.getLiveData().observe(viewLifecycleOwner) {

            when (it) {
                is TrackAdapterData.ButtonClick -> {
                    //vModel.clearPlaylist()
                }

                is TrackAdapterData.ScrollTracksList -> {
                    //scrollTracksList(it.position)
                }

                is TrackAdapterData.TrackClick -> {
                    //vModel.onTrackClicked(it.track)

                    val direction =
                        PlaylistScreenFragmentDirections.actionPlaylistScreenFragmentToPlayerScreenFragment(
                            it.track.trackId
                        )

                    findNavController().navigate(direction)
                }

                is TrackAdapterData.TrackLongClick -> {
                    confirm(
                        title = getString(R.string.playlist_menu_delete_track),
                        text = getString(R.string.playlist_menu_delete_track_question).replace(
                            "[track]",
                            it.track.trackName
                        ),
                        onConfirm = {
                            vModel.deleteTrack(it.track)
                        }
                    )
                }
            }
        }

        //инициализация BottomSheet

        tracksBS = BottomSheetBehavior.from(binding.playlistTracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        tracksBS.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    vModel.setTracksBsState(opened = true)
                } else {
                    vModel.setTracksBsState(opened = false)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        menuBS = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBS.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                binding.overlay.isVisible = if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    vModel.setMenuBsState(opened = false)
                    enableMenuDrag(true)
                    false
                } else {
                    enableMenuDrag(false)
                    true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (1 + slideOffset) / 2
            }
        })

        //устанавливаем высоту экрана так, чтобы список треков был видет на альбомной ориентации

        binding.root.post {
            val metrics = Resources.getSystem().displayMetrics
            val expander = binding.spaceExpander

            val params: ViewGroup.LayoutParams = expander.layoutParams

            if (binding.cover.height > metrics.heightPixels) {

                params.height = metrics.heightPixels / 2

                binding.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                    setTracksBSPeekHeight()
                }

            } else {
                params.height = 50
            }

            setTracksBSPeekHeight()

            expander.layoutParams = params
        }
    }

    private fun enableMenuDrag(isEnabled: Boolean) {
        (tracksBS as LockableBottomSheetBehavior).swipeEnabled = isEnabled
    }

    private fun fillPlaylistInfo(playlist: Playlist) {

        val playlistInMenu = binding.playlistInMenu

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

                Glide.with(playlistInMenu.cover).load(uri).into(playlistInMenu.cover)
            }

            playlistInMenu.title.text = title
            playlistInMenu.tracksTotal.text =
                declension(tracksTotal, getString(R.string.track_counter_declination))
        }
    }

    private fun setTracksBSPeekHeight() {

        val metrics = Resources.getSystem().displayMetrics
        val expander = binding.spaceExpander
        val tracksLL = binding.playlistTracksBottomSheet

        binding.root.post {

            val markY = getYPosition(expander)

            val dY = markY -  getYPosition(tracksLL)

            val forTest = true

            if(forTest || binding.cover.height > metrics.heightPixels){

                //тут мы вычисляем высоту подъема указанным в вопросе способом

                tracksBS.peekHeight =
                    metrics.heightPixels - getYPosition(expander)

                if(markY<metrics.heightPixels) {
                    tracksLL.visibility = View.VISIBLE
                }
                else{
                    tracksLL.visibility = View.INVISIBLE
                }
            }
            else{

                //тут мы вычисляем высоту подъема другим способом

                tracksLL.visibility = View.VISIBLE

                if(abs(dY)>1) {

                    tracksBS.peekHeight -= dY

                    lifecycleScope.launch {
                        delay(100)
                        setTracksBSPeekHeight()
                    }
                }
            }
        }
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun confirm(title: String, text: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.AlertDialogTheme
        ).setTitle(title).setMessage(text)
            .setNegativeButton(R.string.NO) { _, _ ->

            }.setPositiveButton(R.string.YES) { _, _ ->
                onConfirm()
            }.show()
    }

    companion object {
        const val PLAYLIST_ID = "playlistId"
    }
}