package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.ui.favorite.playlists.PlayListsTabFragment
import com.example.playlistmaker.ui.favorite.tracks.FavoriteTracksTabFragment
import com.example.playlistmaker.viewModels.favorite.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment() : Fragment() {

    private var _binding: ActivityMediaLibraryBinding? = null

    private val binding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null

    private val vModel: MediaLibraryViewModel by viewModel()

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

        val tabs: List<MediaLibraryTabsType> = listOf(
            MediaLibraryTabsType(
                getString(R.string.play_list_favorite_tracks),
                FavoriteTracksTabFragment::class.java
            ),
            MediaLibraryTabsType(
                getString(R.string.play_list_playlists),
                PlayListsTabFragment::class.java
            )
        )

        binding.viewPager.adapter =
            MediaLibraryViewPagerAdapter(tabs, childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabs[position].title
        }

        tabMediator?.attach()
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }
}