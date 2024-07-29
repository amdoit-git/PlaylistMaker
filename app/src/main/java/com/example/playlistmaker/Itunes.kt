package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface Itunes {

    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("media") media:String = "music",
        @Query("entity") entity:String = "song",//song or musicTrack
        @Query("country") country:String = Locale.getDefault().country.uppercase()//RU || US || GB || DE || RU
    ): Call<ItunesTrackList>
}