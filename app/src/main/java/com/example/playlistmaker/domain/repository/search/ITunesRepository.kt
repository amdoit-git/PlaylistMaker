package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track

interface ITunesRepository {

    suspend fun search(text: String): ConsumerData<List<Track>>
}