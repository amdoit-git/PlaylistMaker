package com.example.playlistmaker.common.domain.repository

import com.example.playlistmaker.common.domain.models.EmailData
import com.example.playlistmaker.common.domain.models.ShareData

interface ExternalNavigatorRepository {

    fun openUrl(url:String, chooseApp:Boolean = false):Boolean

    fun share(data: ShareData, chooseApp:Boolean = false):Boolean

    fun sendEmail(data: EmailData, chooseApp:Boolean = false):Boolean
}