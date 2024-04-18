package com.example.playlistmaker

data class ItunesTrack(
    val trackId:Int,
    val artistId:Int,
    val kind:String?,
    val trackName: String?,//название трека
    val artistName: String?,//имя исполнителя
    val trackTimeMillis: Int,//293000
    val artworkUrl100: String?,//ссылка на обложку
    val previewUrl:String?//ссылка на трек
)
