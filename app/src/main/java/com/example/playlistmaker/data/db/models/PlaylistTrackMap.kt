package com.example.playlistmaker.data.db.models

import androidx.room.Entity

@Entity(tableName = "playlist_track_map", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackMap(
    val playlistId: Int, val trackId: Int, val addedDate: Int
)
