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
    var isPlaying: Boolean = false, //играет ли трек в данный момент
    var isLiked: Boolean = false,//трек добавлен в понравившиеся
    var inFavorite: Boolean = false//трек добавлен в избранное
) {
    fun getTrack(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName ?: "-",
            artistName = artistName ?: "-",
            trackTime = trackTime ?: "00:00",
            trackCover = trackCover ?: "",
            previewUrl = previewUrl ?: "",
            albumName = albumName ?: "-",
            albumYear = albumYear ?: "-",
            genre = genre ?: "-",
            country = country ?: "-",
            isPlaying = isPlaying,
            isLiked = isLiked,
            inFavorite = inFavorite
        )
    }
}
