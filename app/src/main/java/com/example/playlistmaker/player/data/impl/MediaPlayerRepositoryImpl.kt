package com.example.playlistmaker.player.data.impl

import com.example.playlistmaker.player.data.MediaPlayerService
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayerService: MediaPlayerService) :
    MediaPlayerRepository {

    override fun setDisplayPorts(
        forTime: ((Int) -> Unit)?,
        forDuration: ((Int) -> Unit)?,
        forStop: (() -> Unit)?,
        forError: (() -> Unit)?
    ) {
        mediaPlayerService.setDisplayPorts(forTime, forDuration, forStop, forError)
    }

    override fun resetDisplayPorts() {
        mediaPlayerService.resetDisplayPorts()
    }

    override fun setDataSource(url: String) {
        mediaPlayerService.setDataSource(url)
    }

    override fun play(): Boolean {
        return mediaPlayerService.play()
    }

    override fun pause() {
        mediaPlayerService.pause()
    }

    override fun stop() {
        mediaPlayerService.stop()
    }

    override fun getPosition(): Int {
        return mediaPlayerService.getPosition()
    }

    override fun setPosition(currentPosition: Int) {
        mediaPlayerService.setPosition(currentPosition)
    }

    override fun destroy() {
        mediaPlayerService.destroy()
    }
}