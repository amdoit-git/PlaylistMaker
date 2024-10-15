package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.models.RoomPlaylist
import com.example.playlistmaker.data.impl.favorite.playlists.ImageSaver
import com.example.playlistmaker.domain.models.Playlist

object PlaylistToRoomPlaylistMapper {

    private fun timestamp(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }

    fun map(playlist: Playlist): RoomPlaylist = RoomPlaylist(
        playlistId = 0,
        title = playlist.title,
        description = playlist.description,
        coverFileName = "",
        tracksTotal = 0,
        addedDate = timestamp(),
        lastMod = timestamp()
    )

    fun map(playlist: RoomPlaylist, saver: ImageSaver): Playlist = Playlist(
        id = playlist.playlistId,
        title = playlist.title,
        description = playlist.description,
        coverUri = saver.coverUriFromFile(playlist.coverFileName),
        tracksTotal = playlist.tracksTotal
    )

    fun map(playlists: List<RoomPlaylist>, saver: ImageSaver): List<Playlist> =
        playlists.map { playlist ->
            map(playlist, saver)
        }
}