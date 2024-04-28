package com.example.playlistmaker

data class ItunesError(
    val errorMessage:String,
    val queryParameters:String? = null
)
