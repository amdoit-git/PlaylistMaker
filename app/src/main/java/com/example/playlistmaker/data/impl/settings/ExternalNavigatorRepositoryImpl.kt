package com.example.playlistmaker.data.impl.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.domain.models.ShareData
import com.example.playlistmaker.domain.repository.settings.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(private val context: Context) :
    ExternalNavigatorRepository {

    override fun openUrl(url: String, chooseApp: Boolean): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        return startActivity(intent, chooseApp)
    }

    override fun share(data: ShareData, chooseApp: Boolean): Boolean {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, data.text)
        intent.type = "text/plain"
        return startActivity(intent, chooseApp)
    }

    override fun sendEmail(data: EmailData, chooseApp: Boolean): Boolean {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
        intent.putExtra(Intent.EXTRA_SUBJECT, data.subject)
        intent.putExtra(Intent.EXTRA_TEXT, data.text)
        return startActivity(intent, chooseApp)
    }

    private fun startActivity(intent: Intent, chooseApp: Boolean): Boolean {
        try {
            if (chooseApp) {
                val shareIntent = Intent.createChooser(intent, null)
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(shareIntent)
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            return true
        } catch (er: ActivityNotFoundException) {
            return false
        }
    }
}