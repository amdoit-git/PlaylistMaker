package com.example.playlistmaker

import com.example.playlistmaker.search.data.dto.ItunesTrackList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Itunes {

    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("media") media:String = "music",
        @Query("entity") entity:String = "song",//song or musicTrack
        @Query("country") country:String = "RU"//US || GB || DE || RU
    ): Call<ItunesTrackList>
}