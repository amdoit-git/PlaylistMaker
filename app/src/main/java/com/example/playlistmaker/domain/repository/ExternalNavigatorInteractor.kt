package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.domain.models.ShareData

interface ExternalNavigatorInteractor {

    fun openUrl(url:String, chooseApp:Boolean = false):Boolean

    fun share(data: ShareData, chooseApp:Boolean = false):Boolean

    fun sendEmail(data: EmailData, chooseApp:Boolean = false):Boolean
}