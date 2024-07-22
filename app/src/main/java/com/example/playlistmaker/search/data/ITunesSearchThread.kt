package com.example.playlistmaker.search.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.data.dto.ITunesTrackToTrackMapper
import com.example.playlistmaker.search.data.dto.Itunes
import java.io.IOException

class ITunesSearchThread(
    private val api: Itunes,
    private val searchText: String,
    private val consumer: Consumer<List<Track>>,
    private val context: Context
) : Thread() {

    private val handler = Handler(Looper.getMainLooper())

    override fun run() {

        try {

            val response = api.search(text = searchText).execute()

            if (!isInterrupted) {

                when (response.code()) {
                    200 -> {

                        response.body()?.let {
                            if (it.results.isNotEmpty()) {
                                handler.post {
                                    consumer.consume(
                                        ConsumerData.Data(
                                            value = ITunesTrackToTrackMapper.map(
                                                itunesTracks = it.results,
                                                context = context
                                            )
                                        )
                                    )
                                }
                                return
                            }
                        }

                        handler.post {
                            consumer.consume(
                                ConsumerData.Error(
                                    code = 404, message = "No tracks found for\"$searchText\""
                                )
                            )
                        }
                    }

                    else -> {

                        handler.post {
                            consumer.consume(
                                ConsumerData.Error(
                                    code = response.code(),
                                    message = "The server rejected our request with an error. One search for \"$searchText\""
                                )
                            )
                        }
                    }
                }
            }

        } catch (error: IOException) {

            if (!isInterrupted) {

                handler.post {
                    consumer.consume(
                        ConsumerData.Error(
                            code = 502,
                            message = "Some error occurred when search for \"$searchText\""
                        )
                    )
                }
            }
        }

        super.run()
    }
}