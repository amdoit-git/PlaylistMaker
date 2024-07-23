package com.example.playlistmaker.player.data.impl

import com.example.playlistmaker.player.data.MediaPlayerService
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    override fun setDisplayPorts(
        forTime: ((Int) -> Unit)?,
        forDuration: ((Int) -> Unit)?,
        forStop: (() -> Unit)?,
        forError: (() -> Unit)?
    ) {
        MediaPlayerService.setDisplayPorts(forTime, forDuration, forStop, forError)
    }

    override fun resetDisplayPorts() {
        MediaPlayerService.resetDisplayPorts()
    }

    override fun setDataSource(url: String) {
        MediaPlayerService.setDataSource(url)
    }

    override fun play(): Boolean {
        return MediaPlayerService.play()
    }

    override fun pause() {
        MediaPlayerService.pause()
    }

    override fun stop() {
        MediaPlayerService.stop()
    }

    override fun getPosition(): Int {
        return MediaPlayerService.getPosition()
    }

    override fun setPosition(currentPosition: Int) {
        MediaPlayerService.setPosition(currentPosition)
    }

    override fun destroy() {
        MediaPlayerService.destroy()
    }
}