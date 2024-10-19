package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PlaylistScreenFragment() : Fragment() {

    //private val vModel: PlaylistScreenViewModel by viewModel()

    private var _binding: FragmentPlaylistScreenBinding? = null

    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        binding.root.post {
            setTracksBSPeekHeight()
        }
    }

    private fun setTracksBSPeekHeight() {
        bottomSheetBehavior.peekHeight =
            Resources.getSystem().displayMetrics.heightPixels - getYPosition(binding.playlistInfoContainer) - binding.playlistInfoContainer.height
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }
}