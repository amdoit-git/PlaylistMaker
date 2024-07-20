package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.presentation.SingleEventLiveData
import com.example.playlistmaker.creator.Creator

class SearchViewModel : ViewModel() {

    private val history = Creator.provideTracksHistoryInteractor()
    private val iTunes = Creator.provideITunesInteractor()

    private var searchTextLiveData = SingleEventLiveData<String>()
    private val trackListLiveData = MutableLiveData<TRACK_LIST_STATE>()

    private val handler = Handler(Looper.getMainLooper())
    private val obj = Any()

    private var searchText: String = ""
    private var textInFocus: Boolean = false
    private var STATE = TRACK_LIST_STATE.FIRST_VISIT

    fun onCreate() {
        searchTextLiveData.postValue(searchText)
    }

    fun getSearchTextLiveData(): LiveData<String> {
        return searchTextLiveData
    }

    fun onTextChanged(text: String) {

        searchText = text

        handler.removeCallbacksAndMessages(obj)

        handler.postAtTime({
            search(searchText)
        }, obj, 2000L + SystemClock.uptimeMillis())

        switchHistoryVisibility()
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

            trackListLiveData.postValue(STATE)
        }
    }

    private fun changeState(newSTATE: TRACK_LIST_STATE) {

        STATE = TRACK_LIST_STATE.changeState(newSTATE, STATE)
    }

    private fun showHistory() {

        changeState(TRACK_LIST_STATE.HISTORY_VISIBLE)

        if (STATE.tracks == null) {
            STATE.tracks = history.getList()
        }

        if (STATE.tracks.isNullOrEmpty()) {
            changeState(TRACK_LIST_STATE.HISTORY_EMPTY)
        }

        trackListLiveData.postValue(STATE)
    }

    fun onActionButton() {

        search(searchText)
    }

    fun onTrackClicked() {

    }

    fun onHistoryClearButtonClicked() {
        history.clear()
        TRACK_LIST_STATE.clear()
        changeState(TRACK_LIST_STATE.HISTORY_EMPTY)
        trackListLiveData.postValue(STATE)
    }

    private fun search(text: String) {

        if (text.isBlank()) return;

        iTunes.search(text, object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {

                when (data) {
                    is ConsumerData.Data -> {
                        when (data.value.size) {
                            0 -> {}
                            else -> {}
                        }
                    }

                    is ConsumerData.Error -> TODO()
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        TRACK_LIST_STATE.clear()
    }
}