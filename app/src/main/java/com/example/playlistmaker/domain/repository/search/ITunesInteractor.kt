package com.example.playlistmaker.domain.repository.search

import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface ITunesInteractor {
    suspend fun search(text: String): Flow<ConsumerData<List<Track>>>
}