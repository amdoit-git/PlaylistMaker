package com.example.playlistmaker.viewModels.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.consumer.ConsumerData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.search.TracksHistoryInteractor
import com.example.playlistmaker.domain.repository.search.ITunesInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val history: TracksHistoryInteractor,
    private val iTunes: ITunesInteractor
) : ViewModel() {

    private val liveData = LiveDataWithStartDataSet<SearchData>()

    private var searchDelayJob: Job? = null

    private var searchText: String = ""
    private var textInFocus: Boolean = false
    private var STATE = TrackListState.FIRST_VISIT
    private var tracksInHistory: List<Track>? = null

    private var trackClickAllowed = true

    private var iTunesSearchJob: Job? = null

    fun getLiveData(): LiveData<SearchData> {
        return liveData
    }

    fun onTextChanged(text: String) {

        searchText = text

        searchDelayJob?.cancel()

        searchDelayJob = viewModelScope.launch(Dispatchers.Main) {
            delay(2000L)
            searchOnITunes()
        }

        switchHistoryVisibility()

        liveData.setStartValue(SearchData.SearchText(searchText, textInFocus))
    }

    fun onActionButton() {
        searchOnITunes()
        searchDelayJob?.cancel()
    }

    fun onFocusChanged(hasFocus: Boolean) {

        textInFocus = hasFocus

        switchHistoryVisibility()

        liveData.setStartValue(SearchData.SearchText(searchText, textInFocus))
    }

    private fun switchHistoryVisibility() {

        if (textInFocus && searchText.isEmpty()) {

            viewModelScope.launch(Dispatchers.Main) {
                showHistory()
            }

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

        tracks?.let {
            liveData.setSingleEventValue(SearchData.ScrollTracksList(0))
        }
    }

    private suspend fun showHistory() {

        if (tracksInHistory == null) {
            tracksInHistory = history.getAll()
        }

        if (tracksInHistory.isNullOrEmpty()) {
            changeState(TrackListState.HISTORY_EMPTY)
        } else {
            changeState(TrackListState.HISTORY_VISIBLE, tracksInHistory)
        }

        cancelSearch()
    }

    private fun cancelSearch() {
        iTunesSearchJob?.cancel()
    }

    fun onTrackClicked(track: Track) {

        if (!isClickAllowed()) return Unit;

        viewModelScope.launch(Dispatchers.Main) {
            history.save(track)
        }

        liveData.setSingleEventValue(SearchData.OpenPlayerScreen(history.toJson(track)))

        tracksInHistory?.let { list ->
            tracksInHistory =
                list.partition { it.trackId == track.trackId }.let { it.first + it.second }
        }

        if (STATE == TrackListState.HISTORY_VISIBLE) {

            liveData.setSingleEventValue(SearchData.MoveToTop(track))//showHistory()

            STATE.tracks = tracksInHistory

            liveData.setStartValue(SearchData.TrackList(STATE))
        }
    }

    private fun isClickAllowed(): Boolean {
        if (trackClickAllowed) {
            trackClickAllowed = false
            viewModelScope.launch(Dispatchers.Main) {
                delay(1000L)
                trackClickAllowed = true
            }
            return true
        }
        return false
    }

    fun clearHistory() {
        tracksInHistory = null
        viewModelScope.launch(Dispatchers.Main) {
            history.clear()
        }
        changeState(TrackListState.HISTORY_EMPTY)
    }

    fun searchOnITunes() {

        if (searchText.isBlank()) return;

        liveData.setValue(SearchData.ProgressBar(true))

        iTunesSearchJob = viewModelScope.launch(Dispatchers.Main) {

            try {

                iTunes.search(searchText.trim())
                    .flowOn(Dispatchers.Main)
                    .collect { data ->

                        when (data) {
                            is ConsumerData.Data -> changeState(
                                TrackListState.SEARCH_VISIBLE,
                                data.value
                            )

                            is ConsumerData.Error -> {
                                when (data.code) {
                                    404 -> changeState(TrackListState.SEARCH_EMPTY)
                                    else -> changeState(TrackListState.SEARCH_FAIL)
                                }
                            }
                        }

                        liveData.setValue(SearchData.ProgressBar(false))
                    }
            } catch (error: CancellationException) {
                liveData.setValue(SearchData.ProgressBar(false))
            }
        }
    }
}