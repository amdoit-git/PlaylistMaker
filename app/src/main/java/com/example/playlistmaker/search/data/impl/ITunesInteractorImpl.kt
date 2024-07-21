package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.ITunesSearchThread
import com.example.playlistmaker.search.data.dto.Itunes
import com.example.playlistmaker.search.domain.repository.ITunesInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesInteractorImpl(private val context: Context) : ITunesInteractor {

    private val baseUrl: String = "https://itunes.apple.com"
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val api: Itunes = retrofit.create(Itunes::class.java)
    private var thread: ITunesSearchThread? = null

    override fun search(text: String, consumer: Consumer<List<Track>>) {

        if (!isConnected()) {
            consumer.consume(ConsumerData.Error(code = -1, message = "No internet connection"))
            return
        }

        thread?.let {
            if (it.isAlive) {
                it.interrupt()
            }
        }

        thread =
            ITunesSearchThread(api = api, searchText = text, consumer = consumer)

        thread?.start()
    }

    override fun cancel() {
        thread?.interrupt()
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
