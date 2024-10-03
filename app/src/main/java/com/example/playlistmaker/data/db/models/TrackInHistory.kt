package com.example.playlistmaker.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_history", indices = [Index(value = ["addedDate"])])
data class TrackInHistory(
    @PrimaryKey val trackId: Int, //id трека
    val addedDate: Int//Время открытия трека на экране плеера
)
