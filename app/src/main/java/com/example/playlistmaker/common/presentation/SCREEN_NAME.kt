package com.example.playlistmaker.common.presentation

import android.app.Activity
import com.example.playlistmaker.PlayListActivity
import com.example.playlistmaker.player.ui.PlayerScreenActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

enum class SCREEN_NAME(val className: Class<out Activity>) {
    SEARCH(SearchActivity::class.java),
    PLAYLIST(PlayListActivity::class.java),
    PLAYER(PlayerScreenActivity::class.java),
    SETTINGS(SettingsActivity::class.java);
}