package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.domain.repository.TracksHistoryInteractor
import com.example.playlistmaker.common.presentation.LiveDataWithStartDataSet
import com.example.playlistmaker.common.presentation.ScreenName
import com.example.playlistmaker.search.domain.repository.ITunesInteractor

class SearchViewModel(
    private val context: Context,
    private val history: TracksHistoryInteractor,
    private val iTunes: ITunesInteractor
) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<SearchData>()

    private val handler = Handler(Looper.getMainLooper())
    private val obj = Any()

    private var searchText: String = ""
    private var textInFocus: Boolean = false
    private var STATE = TrackListState.FIRST_VISIT
    private var tracksInHistory: List<Track>? = null

    private var trackClickAllowed = true

    fun getLiveData(): LiveData<SearchData> {
        return liveData
    }


    fun onTextChanged(text: String) {

        searchText = text

        handler.removeCallbacksAndMessages(obj)

        handler.postAtTime({
            searchOnITunes()
        }, obj, 2000L + SystemClock.uptimeMillis())

        switchHistoryVisibility()

        liveData.setStartValue(SearchData.SearchText(searchText))
    }

    fun onActionButton() {
        searchOnITunes()
        handler.removeCallbacksAndMessages(obj)
    }

    fun onFocusChanged(hasFocus: Boolean) {

        textInFocus = hasFocus

        switchHistoryVisibility()
    }

    private fun switchHistoryVisibility() {

        if (textInFocus && searchText.isEmpty()) {

            showHistory()

        } else if (STATE == TrackListState.HISTORY_VISIBLE) {

            changeState(TrackListState.HISTORY_GONE)
        }
    }

    private fun changeState(newSTATE: TrackListState, tracks: List<Track>? = null) {

        STATE = newSTATE

        tracks?.let {
            STATE.tracks = tracks
        }

        if (STATE == TrackListState.SEARCH_VISIBLE) {
            //история просмотров будет не актуальна, потому очищаем ее
            tracksInHistory = null
        }

        liveData.setValue(SearchData.TrackList(STATE))
    }

    private fun showHistory() {

        if (tracksInHistory == null) {
            tracksInHistory = history.getList()
        }

        if (tracksInHistory.isNullOrEmpty()) {
            changeState(TrackListState.HISTORY_EMPTY)
        } else {
            changeState(TrackListState.HISTORY_VISIBLE, tracksInHistory)
        }

        cancelSearch()
    }

    private fun cancelSearch() {
        iTunes.cancelSearch()
        liveData.setValue(SearchData.ProgressBar(false))
    }

    fun onTrackClicked(track: Track) {

        if (!isClickAllowed()) return Unit;

        history.save(track)

        val intent = Intent(context, ScreenName.PLAYER.className)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.putExtra("track", history.toJson(track));

        context.startActivity(intent)

        tracksInHistory = null

        if (STATE == TrackListState.HISTORY_VISIBLE) {
            liveData.setSingleEventValue(SearchData.MoveToTop(track))
            //showHistory()
        }
    }

    private fun isClickAllowed(): Boolean {
        if (trackClickAllowed) {
            trackClickAllowed = false
            handler.postDelayed({
                trackClickAllowed = true
            }, 1000L)
            return true
        }
        return false
    }

    fun clearHistory() {
        tracksInHistory = null
        history.clear()
        changeState(TrackListState.HISTORY_EMPTY)
    }

    fun searchOnITunes() {

        if (searchText.isBlank()) return;

        liveData.setValue(SearchData.ProgressBar(true))

        iTunes.search(searchText.trim(), object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {

                when (data) {
                    is ConsumerData.Data -> changeState(TrackListState.SEARCH_VISIBLE, data.value)

                    is ConsumerData.Error -> {
                        when (data.code) {
                            404 -> changeState(TrackListState.SEARCH_EMPTY)
                            else -> changeState(TrackListState.SEARCH_FAIL)
                        }
                    }
                }

                liveData.setValue(SearchData.ProgressBar(false))
            }
        })
    }

    override fun onCleared() {
        iTunes.destroy()
        super.onCleared()
    }
}