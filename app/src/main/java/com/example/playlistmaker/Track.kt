package com.example.testapp

data class Track(
    val trackId:Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl:String,
    var isPlaying:Boolean = false
)
