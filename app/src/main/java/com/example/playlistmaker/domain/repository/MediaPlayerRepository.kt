package com.example.playlistmaker.domain.repository

interface MediaPlayerRepository {

    fun setDisplayPorts(
        forTime: ((Int) -> Unit)?,
        forDuration: ((Int) -> Unit)?,
        forStop: (() -> Unit)?,
        forError: (() -> Unit)
    )

    fun setDataSource(url: String)

    fun play(): Boolean

    fun pause()

    fun stop()

    fun destroy()

    fun getPosition(): Int

    fun setPosition(currentPosition: Int)

}