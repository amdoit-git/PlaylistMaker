package com.example.playlistmaker.search.presentation

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.common.domain.consumer.Consumer
import com.example.playlistmaker.common.domain.consumer.ConsumerData
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.presentation.LiveDataWithStartDataSet
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.PlayerScreenActivity

class SearchViewModel(private val application: Application) : ViewModel() {

    private val history = Creator.provideTracksHistoryInteractor()
    private val iTunes = Creator.provideITunesInteractor(application.applicationContext)

    private val liveData = LiveDataWithStartDataSet<SearchData>()

    private val handler = Handler(Looper.getMainLooper())
    private val obj = Any()

    private var searchText: String = ""
    private var textInFocus: Boolean = false
    private var STATE = TRACK_LIST_STATE.FIRST_VISIT
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

        liveData.setValueForStartOnly(SearchData.SearchText(searchText))
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

        liveData.setValue(SearchData.TrackList(STATE))
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

    fun onTrackClicked(track: Track) {

        if (!isClickAllowed()) return Unit;

        history.save(track)

        val intent = Intent(application.applicationContext, PlayerScreenActivity::class.java)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.putExtra("track", history.toJson(track));

        application.applicationContext.startActivity(intent)

        tracksInHistory = null

        if (STATE == TRACK_LIST_STATE.HISTORY_VISIBLE) {
            liveData.setValueOnce(SearchData.MoveToTop(track))
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
        changeState(TRACK_LIST_STATE.HISTORY_EMPTY)
    }

    fun searchOnITunes() {

        if (searchText.isBlank()) return;

        liveData.setValue(SearchData.ProgressBar(true))

        iTunes.search(searchText, object : Consumer<List<Track>> {
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

                liveData.setValue(SearchData.ProgressBar(false))
            }
        })
    }

    override fun onCleared() {
        iTunes.cancel()
        super.onCleared()
    }

    class Factory(private val application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(application) as T
        }
    }
}