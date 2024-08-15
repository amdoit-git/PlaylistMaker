package com.example.playlistmaker.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.viewModels.favorite.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel: MediaLibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

        val tabs: List<TabType> = listOf(
            TabType(
                getString(R.string.play_list_favorite_tracks),
                FavoriteTracksFragment::class.java
            ),
            TabType(getString(R.string.play_list_playlists), PlayListFragment::class.java)
        )

        binding.viewPager.adapter =
            PlayListViewPagerAdapter(tabs, supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabs[position].title
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}