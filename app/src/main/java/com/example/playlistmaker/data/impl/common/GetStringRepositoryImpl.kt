package com.example.playlistmaker.data.impl.common

import android.content.Context
import com.example.playlistmaker.domain.repository.common.GetStringRepository

class GetStringRepositoryImpl(private val context: Context) : GetStringRepository {
    override fun get(id: Int): String {
        return context.getString(id)
    }
}