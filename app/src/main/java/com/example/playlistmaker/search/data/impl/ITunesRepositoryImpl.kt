package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.Itunes
import com.example.playlistmaker.search.data.dto.ITunesTrackToTrackMapper
import com.example.playlistmaker.search.domain.repository.ITunesRepository
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class ITunesRepositoryImpl(private val context: Context, private val api: Itunes) :
    Thread(), ITunesRepository {

    private val isTerminated = AtomicBoolean(false)
    private val lock = Object()
    private var searchText = ""
    private val handler = Handler(Looper.getMainLooper())
    private var consumer: Consumer<List<Track>>? = null

    init {
        this.start()
    }

    override fun search(text: String, consumer: Consumer<List<Track>>) {

        if (!isConnected()) {
            consumer.consume(ConsumerData.Error(code = -1, message = "No internet connection"))
            return
        }

        this.doSearch(text, consumer)
    }

    override fun cancel() {
        synchronized(searchText) {
            if (searchText.isEmpty()) return
        }
        doSearch(text = "", null)
    }

    override fun destroy() {
        isTerminated.set(true)
        this.interrupt()
    }

    private fun doSearch(text: String, consumer: Consumer<List<Track>>?) {
        synchronized(searchText) {
            this.searchText = text
            this.consumer = consumer
        }
        synchronized(lock) {
            lock.notify();
        }
    }

    private fun runSearch(text: String): SearchState {

        if (text.isBlank()) return SearchState.SEARCH_COMPLETE

        try {

            val response = api.search(text).execute()

            if (text != getSearchText()) {
                return SearchState.NEW_SEARCH_NEEDED
            }

            when (response.code()) {
                200 -> {
                    response.body()?.let {
                        if (it.results.isNotEmpty()) {
                            postResultsToMainThread(
                                ConsumerData.Data(
                                    value = ITunesTrackToTrackMapper.map(
                                        itunesTracks = it.results,
                                        context = context
                                    )
                                )
                            )
                            return SearchState.SEARCH_COMPLETE
                        }
                    }

                    postResultsToMainThread(
                        ConsumerData.Error(
                            code = 404, message = "No tracks found for\"$searchText\""
                        )
                    )
                }

                else -> {
                    postResultsToMainThread(
                        ConsumerData.Error(
                            code = response.code(),
                            message = "The server rejected our request with an error. One search for \"$searchText\""
                        )
                    )
                }
            }

        } catch (error: IOException) {

            if (text != getSearchText()) {
                return SearchState.NEW_SEARCH_NEEDED
            }

            postResultsToMainThread(
                ConsumerData.Error(
                    code = 502,
                    message = "Some error occurred when search for \"$searchText\""
                )
            )
        }

        return SearchState.SEARCH_COMPLETE
    }

    private fun postResultsToMainThread(data: ConsumerData<List<Track>>) {
        handler.post {
            consumer?.consume(data)
        }
    }

    private fun getSearchText(): String {
        synchronized(searchText) {
            return searchText
        }
    }

    override fun run() {
        super.run()

        do {
            synchronized(lock) {
                try {
                    lock.wait()
                } catch (e: InterruptedException) {
                    isTerminated.set(true)
                }
            }

            while (!isTerminated.get() && runSearch(text = getSearchText()) == SearchState.NEW_SEARCH_NEEDED) {
                //запускаем новый поиск если есть новый поисковый запрос
            }

        } while (!isTerminated.get())
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

    enum class SearchState {
        NEW_SEARCH_NEEDED, SEARCH_COMPLETE;
    }
}