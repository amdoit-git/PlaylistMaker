package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.models.PlaylistUpdates
import com.example.playlistmaker.data.db.models.RoomPlaylist
import com.example.playlistmaker.domain.models.Playlist

object PlaylistToPlaylistUpdatesMapper {

    private fun timestamp(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    fun map(playlist: Playlist, coverFileName:String): PlaylistUpdates = PlaylistUpdates(
        playlistId = playlist.id,
        title = playlist.title,
        description = playlist.description,
        coverFileName = coverFileName,
        lastMod = timestamp()
    )
}