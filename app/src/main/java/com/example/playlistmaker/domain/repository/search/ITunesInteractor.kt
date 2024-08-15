package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track

interface ITunesInteractor {
    fun search(text: String, consumer: Consumer<List<Track>>)

    fun cancelSearch()

    fun destroy()
}