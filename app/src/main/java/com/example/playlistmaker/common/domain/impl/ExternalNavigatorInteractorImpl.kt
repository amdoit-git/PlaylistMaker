package com.example.playlistmaker.common.domain.impl

import com.example.playlistmaker.common.domain.models.EmailData
import com.example.playlistmaker.common.domain.models.ShareData
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorInteractor
import com.example.playlistmaker.common.domain.repository.ExternalNavigatorRepository

class ExternalNavigatorInteractorImpl(private val repository: ExternalNavigatorRepository) :
    ExternalNavigatorInteractor {
    override fun openUrl(url: String, chooseApp: Boolean): Boolean {
        return repository.openUrl(url, chooseApp)
    }

    override fun share(data: ShareData, chooseApp: Boolean): Boolean {
        return repository.share(data, chooseApp)
    }

    override fun sendEmail(data: EmailData, chooseApp: Boolean): Boolean {
        return repository.sendEmail(data, chooseApp)
    }
}