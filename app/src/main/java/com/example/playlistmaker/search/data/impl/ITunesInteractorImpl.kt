package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.repository.ITunesInteractor

class ITunesInteractorImpl : ITunesInteractor {
    override fun search(text: String, consumer: Consumer<List<Track>>) {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

}
