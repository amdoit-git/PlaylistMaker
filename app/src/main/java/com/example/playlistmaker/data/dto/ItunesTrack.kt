package com.example.playlistmaker.data.dto

data class ItunesTrack(
    val trackId: Int,
    val artistId: Int,
    val kind: String?,
    val trackName: String?,//название трека
    val artistName: String?,//имя исполнителя
    val trackTimeMillis: Int,//293000
    val artworkUrl100: String?,//ссылка на обложку
    val previewUrl: String?,//ссылка на трек
    val collectionName: String?,//название альбома
    val releaseDate: String?,//Дата выхода альбома "2014-03-15T12:00:00Z"
    val primaryGenreName: String?,//Музыкальный жанр
    val country: String?//country code RUS
)

/*
{
    "wrapperType": "track",
    "kind": "song",
    "artistId": 262864117,
    "collectionId": 896687965,
    "trackId": 896688003,
    "artistName": "Sexy",
    "collectionName": "Summer Dancefloor Vol. 2",
    "trackName": "Go Boom",
    "collectionCensoredName": "Summer Dancefloor Vol. 2",
    "trackCensoredName": "Go Boom (Radio Mix)",
    "collectionArtistId": 36270,
    "collectionArtistName": "Various Artists",
    "artistViewUrl": "https://music.apple.com/ru/artist/sexy/262864117?uo=4",
    "collectionViewUrl": "https://music.apple.com/ru/album/go-boom-radio-mix/896687965?i=896688003&uo=4",
    "trackViewUrl": "https://music.apple.com/ru/album/go-boom-radio-mix/896687965?i=896688003&uo=4",
    "previewUrl": "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/c5/ea/d6/c5ead600-57bb-d29c-3b07-cdb700abd0d0/mzaf_14643568103237614042.plus.aac.p.m4a",
    "artworkUrl30": "https://is1-ssl.mzstatic.com/image/thumb/Music/v4/c0/73/26/c073261b-d045-6e2e-3736-4910fd9960d9/888831453048.jpg/30x30bb.jpg",
    "artworkUrl60": "https://is1-ssl.mzstatic.com/image/thumb/Music/v4/c0/73/26/c073261b-d045-6e2e-3736-4910fd9960d9/888831453048.jpg/60x60bb.jpg",
    "artworkUrl100": "https://is1-ssl.mzstatic.com/image/thumb/Music/v4/c0/73/26/c073261b-d045-6e2e-3736-4910fd9960d9/888831453048.jpg/100x100bb.jpg",
    "collectionPrice": 99.00,
    "trackPrice": 18.00,
    "releaseDate": "2014-03-15T12:00:00Z",
    "collectionExplicitness": "notExplicit",
    "trackExplicitness": "notExplicit",
    "discCount": 1,
    "discNumber": 1,
    "trackCount": 18,
    "trackNumber": 9,
    "trackTimeMillis": 184248,
    "country": "RUS",
    "currency": "RUB",
    "primaryGenreName": "Танцевальная",
    "isStreamable": true
}
*/