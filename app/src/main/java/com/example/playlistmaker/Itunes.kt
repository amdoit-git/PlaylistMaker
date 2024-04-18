package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Itunes {

    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("media") media:String = "music",
        @Query("entity") entity:String = "song",//song or musicTrack
        @Query("country") country:String = "US"//US || GB || DE || RU
    ): Call<ItunesTrackList>
}