package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.viewModels.favorite.MlViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MlFragment() : Fragment() {

    private var _binding: ActivityMediaLibraryBinding? = null

    private val binding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null

    private val viewModel: MlViewModel by viewModels()

    private val history: TracksHistoryInteractor by inject()

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
        _binding = null
        tabMediator = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val tabs: List<TabType> = listOf(
            TabType(
                getString(R.string.play_list_favorite_tracks),
                MlFavoriteTracksTabFragment::class.java
            ),
            TabType(
                getString(R.string.play_list_playlists),
                MlPlayListsTabFragment::class.java
            )
        )

        binding.viewPager.adapter =
            MlViewPagerAdapter(tabs, childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabs[position].title
        }

        tabMediator?.attach()
//
//        job = lifecycleScope.launch(Dispatchers.Main) {
//            delay(3000L)
//
//            val track = history.getAll().get(0)
//
//            val direction = MlFragmentDirections.actionMediaLibraryFragmentToPlayerScreenFragment(history.toJson(track))
//
//            findNavController().navigate(direction)
//        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }
}