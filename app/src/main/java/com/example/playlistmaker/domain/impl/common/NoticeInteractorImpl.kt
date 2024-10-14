package com.example.playlistmaker.domain.impl.common

import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.common.NoticeRepository
import kotlinx.coroutines.flow.Flow

class NoticeInteractorImpl(private val repository: NoticeRepository) : NoticeInteractor {

    override suspend fun setMessage(text: String) {
        repository.setMessage(text)
    }

    override fun subscribe(): Flow<String> {
        return repository.subscribe()
    }
}