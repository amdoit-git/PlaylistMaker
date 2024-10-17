package com.example.playlistmaker.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_favorite", indices = [Index(value = ["addedDate"])])
data class FavoriteMap(
    @PrimaryKey val trackId: Int, //id трека
    val addedDate: Int//Время добавления трека в избранное
)