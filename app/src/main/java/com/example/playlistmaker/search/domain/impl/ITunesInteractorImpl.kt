package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.repository.ITunesInteractor
import com.example.playlistmaker.search.domain.repository.ITunesRepository

class ITunesInteractorImpl(private val repository: ITunesRepository) : ITunesInteractor {

    override fun search(text: String, consumer: Consumer<List<Track>>) {
        repository.search(text, consumer)
    }

    override fun cancelSearch() {
        repository.cancel()
    }
}
