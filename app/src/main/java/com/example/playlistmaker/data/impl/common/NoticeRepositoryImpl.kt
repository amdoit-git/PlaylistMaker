package com.example.playlistmaker.data.impl.common

import com.example.playlistmaker.domain.repository.common.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class NoticeRepositoryImpl(private val noticeFlow: MutableSharedFlow<String>) : NoticeRepository {

    override suspend fun setMessage(text: String) {
        noticeFlow.emit(text)
    }

    override fun subscribe(): Flow<String> {
        return getFlow()
    }

    private fun getFlow(): SharedFlow<String> {
        return noticeFlow
    }
}