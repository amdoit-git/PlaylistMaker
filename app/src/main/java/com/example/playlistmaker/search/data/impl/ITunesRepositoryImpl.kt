package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.ITunesSearchThread
import com.example.playlistmaker.search.data.Itunes
import com.example.playlistmaker.search.domain.repository.ITunesRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesRepositoryImpl(private val context: Context) : ITunesRepository, Thread() {

    private val baseUrl: String = "https://itunes.apple.com"
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val api: Itunes = retrofit.create(Itunes::class.java)
    private val thread: ITunesSearchThread = ITunesSearchThread(
        api = api,
        context = context
    )

    override fun run() {
        super.run()
    }

    init {
        thread.start()
        Log.d("TEST_IT", "thread.start()")
        this.start()
    }

    override fun search(text: String, consumer: Consumer<List<Track>>) {

        if (!isConnected()) {
            consumer.consume(ConsumerData.Error(code = -1, message = "No internet connection"))
            return
        }

        thread.doSearch(text, consumer)
    }

    override fun cancel() {
        thread.cancel()
    }

    override fun destroy() {
        thread.interrupt()
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