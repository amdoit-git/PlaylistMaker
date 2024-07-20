package com.example.playlistmaker.search.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ItunesApi
import com.example.playlistmaker.MusicPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.presentation.TRACK_LIST_STATE
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.dto.ItunesError
import com.example.playlistmaker.search.data.dto.ItunesTrack
import com.example.playlistmaker.search.presentation.SearchViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private val KEY_FOR_SEARCH_FIELD = "SEARCH_FIELD_STATE_KEY"
    private val KEY_FOR_SEARCH_STATE = "STATE_KEY_SEARCH_STATE"
    private val KEY_FOR_SEARCH_STATE_TRACKS = "STATE_KEY_SEARCH_STATE_INFO"
    private var searchText: String = ""
    private val itunesApi: ItunesApi = ItunesApi()
    private var STATE = TRACK_LIST_STATE.FIRST_VISIT
    private lateinit var tracksList: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var textField: SearchTextField

    private lateinit var presenter: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        presenter = ViewModelProvider(this).get(SearchViewModel::class.java)

        tracksList = findViewById(R.id.tracksList)

        adapter = TrackAdapter()

        tracksList.adapter = adapter

        MusicPlayer.setOnCompleteCallback(adapter::updateTracks)

        textField = SearchTextField(
            activity = this,
            editText = findViewById<EditText>(R.id.editText),
            clearButton = findViewById<View>(R.id.editText_delete),
            onTextChanged = ::onTextChanged,
            onAction = ::searchOnItunes,
            onFocusChanged = ::onFocusChanged
        )

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

        binding.noInternetButton.setOnClickListener {
            searchOnItunes(searchText)
        }

        textField.activate()

        presenter.getSearchTextLiveData().observe(this){
            binding.editText.setText(it)
        }

        presenter.onCreate()
    }

    private fun setScreenState(state: TRACK_LIST_STATE, jsonTracks: String = "") {

        val noTracks = findViewById<View>(R.id.no_tracks)
        val noInternet = findViewById<View>(R.id.no_internet)
        val trackListTitle = findViewById<View>(R.id.tracksListTitle)
        val elements: List<View> = listOf(tracksList, noTracks, noInternet, trackListTitle)

        STATE = state
        STATE.jsonTracks = jsonTracks

        elements.forEach { it.visibility = View.GONE }

        when (STATE) {
            TRACK_LIST_STATE.FIRST_VISIT -> {}
            TRACK_LIST_STATE.HISTORY_EMPTY -> {}
            TRACK_LIST_STATE.HISTORY_GONE -> {}
            TRACK_LIST_STATE.HISTORY_VISIBLE -> {
                trackListTitle.visibility = View.VISIBLE
                tracksList.visibility = View.VISIBLE
            }

            TRACK_LIST_STATE.SEARCH_VISIBLE -> {
                tracksList.visibility = View.VISIBLE
            }

            TRACK_LIST_STATE.SEARCH_EMPTY -> {
                noTracks.visibility = View.VISIBLE
            }

            TRACK_LIST_STATE.SEARCH_FAIL -> {
                noInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun onFocusChanged(str: String, hasFocus: Boolean) {

        switchHistoryVisibility(str, hasFocus)
    }

    private fun onTextChanged(str: String, hasFocus: Boolean) {

        searchText = str

        switchHistoryVisibility(str, hasFocus)
    }

    private fun switchHistoryVisibility(str: String, hasFocus: Boolean) {

        val isVisible = str.isEmpty() && hasFocus

        if (isVisible) {

            when (STATE) {
                TRACK_LIST_STATE.FIRST_VISIT -> showHistoryList()
                TRACK_LIST_STATE.HISTORY_VISIBLE -> {}
                TRACK_LIST_STATE.HISTORY_GONE -> setScreenState(TRACK_LIST_STATE.HISTORY_VISIBLE)
                TRACK_LIST_STATE.HISTORY_EMPTY -> {}
                TRACK_LIST_STATE.SEARCH_VISIBLE -> showHistoryList()
                TRACK_LIST_STATE.SEARCH_EMPTY -> showHistoryList()
                TRACK_LIST_STATE.SEARCH_FAIL -> showHistoryList()
            }

        } else if (STATE == TRACK_LIST_STATE.HISTORY_VISIBLE) {

            setScreenState(TRACK_LIST_STATE.HISTORY_GONE)
        }
    }

    private fun showHistoryList() {

        if (adapter.hasClearButton) {
            //История прослущиваний уже на экране. Просто показываем ее

            setScreenState(TRACK_LIST_STATE.HISTORY_VISIBLE)
        } else {
            //Загружаем историю прослущиваний и показываем если она есть

            SearchHistory.loadTracksList()?.let {
                if (it.size > 0) {
                    setScreenState(
                        TRACK_LIST_STATE.HISTORY_VISIBLE, SearchHistory.toJson(it)
                    )
                    showTracks(it, true)
                    tracksList.smoothScrollToPosition(0)
                } else {
                    setScreenState(TRACK_LIST_STATE.HISTORY_EMPTY)
                }
            } ?: run {

                setScreenState(TRACK_LIST_STATE.HISTORY_EMPTY)
            }
        }
    }

    private fun clearHistory() {
        showTracks(emptyList())
        setScreenState(TRACK_LIST_STATE.FIRST_VISIT)
        SearchHistory.clearHistory()
        MusicPlayer.destroy()
    }

    private fun showTracks(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.tracks.clear()

        tracks.forEach {
            it.isPlaying = MusicPlayer.isPlayingNow(it)
            adapter.tracks.add(it)
        }

        if (showClearButton) {

            adapter.showClearButton(::clearHistory) {
                tracksList.scrollToPosition(it)
            }

        } else {
            adapter.showClearButton(null)
        }

        adapter.notifyDataSetChanged()

        if (tracks.none { it.isPlaying }) {
            MusicPlayer.destroy()
        }
    }

    private fun searchOnItunes(textToSearch: String) {
        if (textToSearch.isNotBlank()) {
            itunesApi.search(
                textToSearch.trim(), ::onSearchSuccess, ::onSearchEmpty, ::onSearchFail
            )

            showProgressBar()
        }
    }

    private fun showProgressBar() {
        findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        findViewById<View>(R.id.progressBar).visibility = View.GONE
    }

    private fun getStringResourceByName(aString: String?): String? {

        if (aString != null) {
            try {
                val resId: Int = resources.getIdentifier(aString, "string", packageName);
                return getString(resId);
            } catch (_: Resources.NotFoundException) {
            }
        }

        return null;
    }

    private fun getReleaseYear(releaseDate: String?): String? {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        releaseDate?.let {
            try {
                val date = LocalDate.parse(it, format)
                return date.year.toString()
            } catch (_: DateTimeParseException) {
            }
        }

        return null
    }

    private fun onSearchSuccess(itunesTracks: List<ItunesTrack>) {

        val tracks: MutableList<Track> = mutableListOf()

        for (item in itunesTracks) {

            val track = Track(
                trackId = item.trackId,
                trackName = item.trackName ?: "-",
                artistName = item.artistName ?: "-",
                trackTime = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(item.trackTimeMillis),
                trackCover = item.artworkUrl100 ?: "",
                previewUrl = item.previewUrl ?: "",
                albumName = item.collectionName ?: "-",
                country = getStringResourceByName("country_code_" + item.country) ?: "-",
                genre = item.primaryGenreName ?: "-",
                albumYear = getReleaseYear(item.releaseDate) ?: "-"
            )

            tracks.add(track)
        }

        setScreenState(TRACK_LIST_STATE.SEARCH_VISIBLE, SearchHistory.toJson(tracks))

        showTracks(tracks)

        tracksList.smoothScrollToPosition(0)

        hideProgressBar()
    }

    private fun onSearchEmpty() {

        setScreenState(TRACK_LIST_STATE.SEARCH_EMPTY)

        hideProgressBar()
    }

    private fun onSearchFail(textToSearch: String, error: ItunesError? = null) {

        setScreenState(TRACK_LIST_STATE.SEARCH_FAIL, textToSearch)

        hideProgressBar()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //восстанавливаем состояние при повороте экрана или смене темы

        savedInstanceState.let {

            searchText = it.getString(KEY_FOR_SEARCH_FIELD, "")

            textField.setText(searchText)

            setScreenState(
                state = TRACK_LIST_STATE.find(it.getInt(KEY_FOR_SEARCH_STATE, 0)),
                jsonTracks = it.getString(KEY_FOR_SEARCH_STATE_TRACKS, "")
            )

            when (STATE) {
                TRACK_LIST_STATE.FIRST_VISIT -> {}
                TRACK_LIST_STATE.HISTORY_EMPTY -> {}
                TRACK_LIST_STATE.HISTORY_GONE -> restoreTracksOnStart(STATE.jsonTracks, true)
                TRACK_LIST_STATE.HISTORY_VISIBLE -> restoreTracksOnStart(STATE.jsonTracks, true)
                TRACK_LIST_STATE.SEARCH_VISIBLE -> restoreTracksOnStart(STATE.jsonTracks)
                TRACK_LIST_STATE.SEARCH_EMPTY -> {}
                TRACK_LIST_STATE.SEARCH_FAIL -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FOR_SEARCH_FIELD, searchText)
        outState.putInt(KEY_FOR_SEARCH_STATE, STATE.num)
        outState.putString(KEY_FOR_SEARCH_STATE_TRACKS, SearchHistory.toJson(adapter.tracks))
    }

    private fun restoreTracksOnStart(json: String, showClearButton: Boolean = false) {

        SearchHistory.jsonToTracks(json)?.let {
            showTracks(it, showClearButton)
        } ?: run {
            setScreenState(TRACK_LIST_STATE.FIRST_VISIT)
        }
    }
}