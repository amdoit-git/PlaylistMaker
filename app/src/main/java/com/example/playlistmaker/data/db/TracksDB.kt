package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTracksDao
import com.example.playlistmaker.data.db.models.TrackInDB
import com.example.playlistmaker.data.db.models.TrackInFavorite

@Database(
    version = 1, entities = [
        TrackInDB::class,
        TrackInFavorite::class
    ]
)
abstract class TracksDB : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}