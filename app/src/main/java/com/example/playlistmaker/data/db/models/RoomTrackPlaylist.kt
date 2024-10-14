package com.example.playlistmaker.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists")
data class RoomTrackPlaylist @JvmOverloads constructor(
    @PrimaryKey val trackId: Int, //id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: Int, // Продолжительность трека
    val trackCover: String, // Ссылка на изображение обложки
    val previewUrl: String, //ссылка на аудио или видео файл,
    val albumName: String,//название альбома
    val albumYear: Int,//Дата выхода альбома "2014"
    val genre: String,//Музыкальный жанр
    val country: String//Страна выпустившая альбом
)
