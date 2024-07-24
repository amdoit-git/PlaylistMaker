package com.example.playlistmaker.search.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.dto.ITunesTrackToTrackMapper
import java.io.IOException


class ITunesSearchThread(
    private val api: Itunes,
    private val context: Context
) : Thread() {

    private val handler = Handler(Looper.getMainLooper())
    private val lock = Object()
    private var consumer: Consumer<List<Track>>? = null
    private var searchText = ""
    private var isInterruptedException = false

    fun cancel() {
        synchronized(searchText) {
            if (searchText.isEmpty()) return
        }
        doSearch(text = "", null)
    }

    fun doSearch(text: String, consumer: Consumer<List<Track>>?) {
        synchronized(searchText) {
            this.searchText = text
            this.consumer = consumer
        }
        synchronized(lock) {
            lock.notify();
        }
    }

    private fun runSearch(text: String): Boolean {

        if (text.isBlank()) return false

        try {

            val response = api.search(text).execute()

            if (text != getSearchText()) {
                return true
            }

            when (response.code()) {
                200 -> {

                    response.body()?.let {
                        if (it.results.isNotEmpty()) {
                            handler.post {
                                consumer?.consume(
                                    ConsumerData.Data(
                                        value = ITunesTrackToTrackMapper.map(
                                            itunesTracks = it.results,
                                            context = context
                                        )
                                    )
                                )
                            }
                        }
                    }

                    handler.post {
                        consumer?.consume(
                            ConsumerData.Error(
                                code = 404, message = "No tracks found for\"$searchText\""
                            )
                        )
                    }
                }

                else -> {

                    handler.post {
                        consumer?.consume(
                            ConsumerData.Error(
                                code = response.code(),
                                message = "The server rejected our request with an error. One search for \"$searchText\""
                            )
                        )
                    }
                }
            }

        } catch (error: IOException) {

            if (text != getSearchText()) {
                return true
            }

            handler.post {
                consumer?.consume(
                    ConsumerData.Error(
                        code = 502,
                        message = "Some error occurred when search for \"$searchText\""
                    )
                )
            }
        }

        return false
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
                    isInterruptedException = true
                }
            }

            while (!isInterrupted && !isInterruptedException && runSearch(text = getSearchText())) {
                //запускаем новый поиск если есть новый поисковый запрос
            }

        } while (!isInterrupted && !isInterruptedException)
    }
}