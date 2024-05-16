package com.example.playlistmaker

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.Track
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
    private var STATE = SEARCH_STATE.FIRST_VISIT
    private lateinit var tracksList: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var textField: SearchTextField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tracksList = findViewById<RecyclerView>(R.id.tracksList)

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

        findViewById<View>(R.id.button_to_main_screen).setOnClickListener {
            this.finish()
        }

        findViewById<Button>(R.id.no_internet_button).setOnClickListener {
            searchOnItunes(searchText)
        }

        textField.activate()
    }

    private fun setScreenState(state: SEARCH_STATE, jsonTracks: String = "") {

        val noTracks = findViewById<View>(R.id.no_tracks)
        val noInternet = findViewById<View>(R.id.no_internet)
        val trackListTitle = findViewById<View>(R.id.tracksListTitle)
        val elements: List<View> = listOf(tracksList, noTracks, noInternet, trackListTitle)

        STATE = state
        STATE.jsonTracks = jsonTracks

        elements.forEach { it.visibility = View.GONE }

        when (STATE) {
            SEARCH_STATE.FIRST_VISIT -> {}
            SEARCH_STATE.HISTORY_EMPTY -> {}
            SEARCH_STATE.HISTORY_GONE -> {}
            SEARCH_STATE.HISTORY_TRACKS_VISIBLE -> {
                trackListTitle.visibility = View.VISIBLE
                tracksList.visibility = View.VISIBLE
            }

            SEARCH_STATE.SEARCH_TRACKS_VISIBLE -> {
                tracksList.visibility = View.VISIBLE
            }

            SEARCH_STATE.SEARCH_EMPTY -> {
                noTracks.visibility = View.VISIBLE
            }

            SEARCH_STATE.SEARCH_FAIL -> {
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
                SEARCH_STATE.FIRST_VISIT -> showHistoryList()
                SEARCH_STATE.HISTORY_TRACKS_VISIBLE -> {}
                SEARCH_STATE.HISTORY_GONE -> setScreenState(SEARCH_STATE.HISTORY_TRACKS_VISIBLE)
                SEARCH_STATE.HISTORY_EMPTY -> {}
                SEARCH_STATE.SEARCH_TRACKS_VISIBLE -> showHistoryList()
                SEARCH_STATE.SEARCH_EMPTY -> showHistoryList()
                SEARCH_STATE.SEARCH_FAIL -> showHistoryList()
            }

        } else if (STATE == SEARCH_STATE.HISTORY_TRACKS_VISIBLE) {

            setScreenState(SEARCH_STATE.HISTORY_GONE)
        }
    }

    private fun showHistoryList() {

        if (adapter.hasClearButton) {
            //История прослущиваний уже на экране. Просто показываем ее

            setScreenState(SEARCH_STATE.HISTORY_TRACKS_VISIBLE)
        } else {
            //Загружаем историю прослущиваний и показываем если она есть

            SearchHistory.loadTracksList()?.let {
                if (it.size > 0) {
                    setScreenState(
                        SEARCH_STATE.HISTORY_TRACKS_VISIBLE, SearchHistory.toJson(it)
                    )
                    showTracks(it, true)
                    tracksList.smoothScrollToPosition(0)
                } else {
                    setScreenState(SEARCH_STATE.HISTORY_EMPTY)
                }
            } ?: run {

                setScreenState(SEARCH_STATE.HISTORY_EMPTY)
            }
        }
    }

    private fun clearHistory() {
        showTracks(emptyList())
        setScreenState(SEARCH_STATE.FIRST_VISIT)
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
        }
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
                trackName = item.trackName ?: "название трека",
                artistName = item.artistName ?: "имя исполнителя",
                trackTime = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(item.trackTimeMillis),
                trackCover = item.artworkUrl100 ?: "",
                previewUrl = item.previewUrl ?: "",
                albumName = item.collectionName ?: "",
                country = getStringResourceByName(item.country) ?: "",
                genre = item.primaryGenreName ?: "",
                albumYear = getReleaseYear(item.releaseDate) ?: "0000"
            )

            tracks.add(track)
        }

        setScreenState(SEARCH_STATE.SEARCH_TRACKS_VISIBLE, SearchHistory.toJson(tracks))

        showTracks(tracks)

        tracksList.smoothScrollToPosition(0)
    }

    private fun onSearchEmpty() {

        setScreenState(SEARCH_STATE.SEARCH_EMPTY)
    }

    private fun onSearchFail(textToSearch: String, error: ItunesError? = null) {

        setScreenState(SEARCH_STATE.SEARCH_FAIL, textToSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        //восстанавливаем состояние при повороте экрана или смене темы

        savedInstanceState.let {

            searchText = it.getString(KEY_FOR_SEARCH_FIELD, "")

            textField.setText(searchText)

            setScreenState(
                state = SEARCH_STATE.find(it.getInt(KEY_FOR_SEARCH_STATE, 0)),
                jsonTracks = it.getString(KEY_FOR_SEARCH_STATE_TRACKS, "")
            )

            when (STATE) {
                SEARCH_STATE.FIRST_VISIT -> {}
                SEARCH_STATE.HISTORY_EMPTY -> {}
                SEARCH_STATE.HISTORY_GONE -> restoreTracksOnStart(STATE.jsonTracks, true)
                SEARCH_STATE.HISTORY_TRACKS_VISIBLE -> restoreTracksOnStart(STATE.jsonTracks, true)
                SEARCH_STATE.SEARCH_TRACKS_VISIBLE -> restoreTracksOnStart(STATE.jsonTracks)
                SEARCH_STATE.SEARCH_EMPTY -> {}
                SEARCH_STATE.SEARCH_FAIL -> {}
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
            setScreenState(SEARCH_STATE.FIRST_VISIT)
        }
    }
}