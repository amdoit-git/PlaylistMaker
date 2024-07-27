package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.models.Track

interface ITunesRepository {

    fun search(text: String, consumer: Consumer<List<Track>>)

    fun cancel()

    fun destroy()
}