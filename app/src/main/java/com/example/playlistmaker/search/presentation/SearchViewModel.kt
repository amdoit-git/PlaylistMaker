package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.presentation.LiveDataWithStartDataSet
import com.example.playlistmaker.creator.Creator

class SearchViewModel : ViewModel() {

    private val history = Creator.provideTracksHistoryInteractor()
    private val iTunes = Creator.provideITunesInteractor()

    private val liveData = LiveDataWithStartDataSet<SearchData>()

    private val handler = Handler(Looper.getMainLooper())
    private val obj = Any()

    private var searchText: String = ""
    private var textInFocus: Boolean = false
    private var STATE = TRACK_LIST_STATE.FIRST_VISIT
    private var tracksInHistory: List<Track>? = null


    fun onTextChanged(text: String) {

        searchText = text

        handler.removeCallbacksAndMessages(obj)

        handler.postAtTime({
            search(searchText)
        }, obj, 2000L + SystemClock.uptimeMillis())

        switchHistoryVisibility()

        liveData.setValueForStartOnly(SearchData.SearchText(searchText))
    }

    fun onFocusChanged(hasFocus: Boolean) {

        textInFocus = hasFocus

        switchHistoryVisibility()
    }

    private fun switchHistoryVisibility() {

        if (textInFocus && searchText.isEmpty()) {

            showHistory()

        } else if (STATE == TRACK_LIST_STATE.HISTORY_VISIBLE) {

            changeState(TRACK_LIST_STATE.HISTORY_GONE)
        }
    }

    private fun changeState(newSTATE: TRACK_LIST_STATE, tracks: List<Track>? = null) {

        STATE = newSTATE

        tracks?.let {
            STATE.tracks = tracks
        }

        if (STATE == TRACK_LIST_STATE.SEARCH_VISIBLE) {
            //история просмотров будет не актуальна, потому очищаем ее
            tracksInHistory = null
        }

        liveData.postValue(SearchData.TrackList(STATE))
    }

    private fun showHistory() {

        if (tracksInHistory == null) {
            tracksInHistory = history.getList()
        }

        if (tracksInHistory.isNullOrEmpty()) {
            changeState(TRACK_LIST_STATE.HISTORY_EMPTY)
        } else {
            changeState(TRACK_LIST_STATE.HISTORY_VISIBLE, tracksInHistory)
        }
    }

    fun onActionButton() {

        search(searchText)
    }

    fun onTrackClicked() {

    }

    fun onHistoryClearButtonClicked() {
        tracksInHistory = null
        history.clear()
        changeState(TRACK_LIST_STATE.HISTORY_EMPTY)
    }

    private fun search(text: String) {

        if (text.isBlank()) return;

        liveData.postValue(SearchData.ProgressBar(true))

        iTunes.search(text, object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {

                when (data) {
                    is ConsumerData.Data -> changeState(TRACK_LIST_STATE.SEARCH_VISIBLE, data.value)

                    is ConsumerData.Error -> {
                        when (data.code) {
                            404 -> changeState(TRACK_LIST_STATE.SEARCH_EMPTY)
                            else -> changeState(TRACK_LIST_STATE.SEARCH_FAIL)
                        }
                    }
                }

                liveData.postValue(SearchData.ProgressBar(false))
            }
        })
    }

    override fun onCleared() {
        iTunes.cancel()
        super.onCleared()
    }
}