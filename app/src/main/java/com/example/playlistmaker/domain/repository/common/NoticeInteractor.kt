package com.example.playlistmaker.domain.repository.common

import kotlinx.coroutines.flow.Flow

interface NoticeInteractor {

    suspend fun setMessage(text:String)

    fun subscribe(): Flow<String>
}