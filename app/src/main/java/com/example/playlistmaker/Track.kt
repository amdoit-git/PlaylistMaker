package com.example.testapp

data class Track(
    val trackId: Int, //id трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val trackCover: String, // Ссылка на изображение обложки
    val previewUrl: String, //ссылка на аудио или видео файл,
    val albumName: String,//название альбома
    val albumYear: String,//Дата выхода альбома "2014"
    val genre: String,//Музыкальный жанр
    val country: String,//Страна выпустившая альбом
    var isPlaying: Boolean = false //играет ли трек в данный момент
) {
    fun getTrack(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName ?: "unknown",
            artistName = artistName ?: "unknown",
            trackTime = trackTime ?: "00:00",
            trackCover = trackCover ?: "",
            previewUrl = previewUrl ?: "",
            albumName = albumName ?: "unknown",
            albumYear = albumYear ?: "unknown",
            genre = genre ?: "unknown",
            country = country ?: "unknown",
            isPlaying = isPlaying
        )
    }
}
