package com.example.playlistmaker.common.data

import android.app.Application
import android.content.res.Resources

object GetStringResource {

    private var application: Application? = null

    fun setApplication(application: Application) {
        this.application = application
    }

    fun getByName(aString: String?): String? {

        application?.let {

            val context = it.applicationContext

            if (aString != null) {
                try {
                    val resId: Int =
                        context.resources.getIdentifier(aString, "string", context.packageName);
                    return context.getString(resId)
                } catch (_: Resources.NotFoundException) {
                }
            }
        }

        return null;
    }
}