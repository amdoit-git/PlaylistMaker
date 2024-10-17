package com.example.playlistmaker.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.data.db.dao.HistoryTracksDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.models.FavoriteMap
import com.example.playlistmaker.data.db.models.HistoryMap
import com.example.playlistmaker.data.db.models.PlaylistTrackMap
import com.example.playlistmaker.data.db.models.RoomPlaylist
import com.example.playlistmaker.data.db.models.RoomTrack
import com.example.playlistmaker.data.db.models.RoomTrackPlaylist

@Database(
    version = 1, entities = [
        RoomTrack::class,
        FavoriteMap::class,
        HistoryMap::class,
        RoomPlaylist::class,
        RoomTrackPlaylist::class,
        PlaylistTrackMap::class
    ]
)
abstract class TracksDB : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun historyTracksDao(): HistoryTracksDao
}