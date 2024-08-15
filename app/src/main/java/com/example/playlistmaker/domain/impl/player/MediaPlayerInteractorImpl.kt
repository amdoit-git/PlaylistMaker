package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.repository.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.repository.player.MediaPlayerRepository

class MediaPlayerInteractorImpl(val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    override fun setDisplayPorts(
        forTime: ((Int) -> Unit)?,
        forDuration: ((Int) -> Unit)?,
        forStop: (() -> Unit)?,
        forError: (() -> Unit)?
    ) {
        repository.setDisplayPorts(forTime, forDuration, forStop, forError)
    }

    override fun resetDisplayPorts() {
        repository.resetDisplayPorts()
    }

    override fun setDataSource(url: String) {
        repository.setDataSource(url)
    }

    override fun play(): Boolean {
        return repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun stop() {
        repository.stop()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun getPosition(): Int {
        return repository.getPosition()
    }

    override fun setPosition(currentPosition: Int) {
        repository.setPosition(currentPosition)
    }
}