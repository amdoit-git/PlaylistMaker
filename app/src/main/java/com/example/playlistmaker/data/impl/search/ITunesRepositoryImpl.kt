package com.example.playlistmaker.data.impl.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.api.search.Itunes
import com.example.playlistmaker.data.dto.search.ITunesTrackToTrackMapper
import com.example.playlistmaker.data.dto.search.ItunesTrackList
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.ITunesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class ITunesRepositoryImpl(private val context: Context, private val api: Itunes) :
    ITunesRepository {

    override suspend fun search(text: String): Flow<ConsumerData<List<Track>>> = flow {

        emit(doSearch(text))
    }

    private suspend fun doSearch(text: String): ConsumerData<List<Track>> {

        if (!isConnected()) {
            return ConsumerData.Error(code = -1, message = "No internet connection")
        }

        return withContext(Dispatchers.IO) {

            try {

                val iTunesTracks: ItunesTrackList = api.search(text)

                if (iTunesTracks.results.isNotEmpty()) {
                    ConsumerData.Data(
                        value = ITunesTrackToTrackMapper.map(
                            itunesTracks = iTunesTracks.results,
                            context = context
                        )
                    )
                } else {
                    ConsumerData.Error(
                        code = 404, message = "No tracks found for\"$text\""
                    )
                }

            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                ConsumerData.Error(
                    code = 502,
                    message = "Some error occurred when search for \"$text\""
                )
            }
        }
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