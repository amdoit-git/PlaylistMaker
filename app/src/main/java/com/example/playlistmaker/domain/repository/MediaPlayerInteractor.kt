package com.example.playlistmaker.domain.repository

interface MediaPlayerInteractor {

    fun setDisplayPorts(
        forTime: ((Int) -> Unit)?,
        forDuration: ((Int) -> Unit)?,
        forStop: (() -> Unit)?,
        forError: (() -> Unit)?
    )

    fun resetDisplayPorts()

    fun setDataSource(url: String)

    fun play(): Boolean

    fun pause()

    fun stop()

    fun destroy()

    fun getPosition(): Int

    fun setPosition(currentPosition: Int)
}