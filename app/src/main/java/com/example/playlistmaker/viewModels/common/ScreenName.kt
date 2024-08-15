package com.example.playlistmaker.viewModels.common

import android.app.Activity
import com.example.playlistmaker.ui.favorite.MediaLibraryActivity
import com.example.playlistmaker.ui.player.PlayerScreenActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity

enum class ScreenName(val className: Class<out Activity>) {
    SEARCH(SearchActivity::class.java),
    PLAYLIST(MediaLibraryActivity::class.java),
    PLAYER(PlayerScreenActivity::class.java),
    SETTINGS(SettingsActivity::class.java);
}