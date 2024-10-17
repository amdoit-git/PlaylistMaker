package com.example.playlistmaker.data.db.models

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_track_map",
    primaryKeys = ["playlistId", "trackId"],
    indices = [
        Index(value = ["playlistId", "addedDate"]),
        Index(value = ["trackId", "playlistId", "addedDate"])
    ]
)
data class PlaylistTrackMap(
    val playlistId: Int, val trackId: Int, val addedDate: Int
)
