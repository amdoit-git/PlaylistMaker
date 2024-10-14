package com.example.playlistmaker.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlists", indices = [Index(value = ["lastMod"])])
data class RoomPlaylist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val title: String,
    val description: String,
    val coverFileName: String,//название файла с обложкой (если есть)
    val tracksTotal: Int,//число треков в плейлисте
    val addedDate: Int,//Время создания плейлистаv
    val lastMod: Int//Время обновления плейлиста
)
