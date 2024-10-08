package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.ITunesInteractor
import com.example.playlistmaker.domain.repository.search.ITunesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesInteractorImpl(private val repository: ITunesRepository) : ITunesInteractor {

    override suspend fun search(text: String): Flow<ConsumerData<List<Track>>> = flow {

        repository.search(text).collect { data -> emit(data) }
    }
}
