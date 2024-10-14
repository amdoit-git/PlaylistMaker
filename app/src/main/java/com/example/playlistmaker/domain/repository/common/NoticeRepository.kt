package com.example.playlistmaker.domain.repository.common

import kotlinx.coroutines.flow.Flow

interface NoticeRepository {

    suspend fun setMessage(text:String)

    fun subscribe(): Flow<String>
}