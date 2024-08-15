package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface ITunesRepository {

    fun search(text: String, consumer: Consumer<List<Track>>)

    fun cancel()

    fun destroy()
}