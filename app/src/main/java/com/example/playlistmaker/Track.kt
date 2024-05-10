package com.example.testapp

data class Track(
    val trackId: Int, //id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String, //ссылка на аудио или видео файл
    var isPlaying: Boolean = false //играет ли трек в данный момент
)
