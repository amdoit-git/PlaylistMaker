package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.APP_SETTINGS_PREFERENCES
import com.example.playlistmaker.data.api.search.Itunes
import com.example.playlistmaker.data.db.TracksDB
import com.example.playlistmaker.data.impl.favorite.playlists.ImageSaver
import com.example.playlistmaker.data.impl.player.MediaPlayerService
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<Itunes> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(Itunes::class.java)
    }

    single {
        MediaPlayer()
    }

    single<MediaPlayerService> {
        MediaPlayerService(mediaPlayer = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            APP_SETTINGS_PREFERENCES, MODE_PRIVATE
        )
    }

    single<Gson> {
        Gson()
    }

    single<TracksDB> {
        Room.databaseBuilder(androidContext(), TracksDB::class.java, "tracks_x.db").build()
    }

    factory<ImageSaver> {
        ImageSaver(context = androidContext())
    }
}