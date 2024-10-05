package com.example.playlistmaker.ui.favorite.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentPlayListBinding
import com.example.playlistmaker.ui.favorite.MediaLibraryFragmentDirections
import com.example.playlistmaker.viewModels.favorite.MlPlayListsTabViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsTabFragment : Fragment() {

    private val vModel: MlPlayListsTabViewModel by viewModel()

    private var _binding: FragmentPlayListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btAddNewPlaylist.setOnClickListener {
            val direction =
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToAddNewPlayListFragment()

            findNavController().navigate(direction)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() = PlayListsTabFragment()
    }
}