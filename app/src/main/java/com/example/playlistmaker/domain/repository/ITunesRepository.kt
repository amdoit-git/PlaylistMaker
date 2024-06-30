package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface ITunesRepository {
    fun search(query:String):List<Track>
}