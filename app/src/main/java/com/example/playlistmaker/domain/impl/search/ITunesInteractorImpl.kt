package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.ITunesInteractor
import com.example.playlistmaker.domain.repository.search.ITunesRepository

class ITunesInteractorImpl(private val repository: ITunesRepository) : ITunesInteractor {

    override fun search(text: String, consumer: Consumer<List<Track>>) {
        repository.search(text, consumer)
    }

    override fun cancelSearch() {
        repository.cancel()
    }

    override fun destroy() {
        repository.destroy()
    }


}
