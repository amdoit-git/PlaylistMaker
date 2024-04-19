package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.Track
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private val STATE_KEY_SEARCH_FIELD = "SEARCH_FIELD_STATE_KEY";
    private val STATE_KEY_SEARCH_STATE = "STATE_KEY_SEARCH_STATE";
    private val STATE_KEY_SEARCH_STATE_INFO = "STATE_KEY_SEARCH_STATE_INFO";
    private var searchText: String = "";
    private lateinit var tracksList: RecyclerView;
    private lateinit var noInternet: View;
    private lateinit var noTracks: View;
    private lateinit var adapter: TrackAdapter;
    private val itunesApi: ItunesApi = ItunesApi();
    private val gson = Gson();
    private var STATE = SEARCH_STATE.NO_ACTION;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val b_to_main_screen = findViewById<View>(R.id.button_to_main_screen)
        val editText_delete = findViewById<View>(R.id.editText_delete)
        val editText = findViewById<EditText>(R.id.editText)

        b_to_main_screen.setOnClickListener {
            this.finish();
            MusicPlayer.destroy()
        }

        editText_delete.setOnClickListener {
            editText.setText("")
            closeKeyboard();

            setVisible(tracksList);
            adapter.tracks.clear();
            adapter.notifyDataSetChanged();
            STATE = SEARCH_STATE.NO_ACTION;
            MusicPlayer.destroy()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                editText_delete.visibility = if (editText.text.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                searchText = editText.text.toString()
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchText);
                true
            }
            false
        }

        tracksList = findViewById<RecyclerView>(R.id.tracksList)
        noTracks = findViewById<View>(R.id.no_tracks);
        noInternet = findViewById<View>(R.id.no_internet);

        val noInternetButton = findViewById<Button>(R.id.no_internet_button)

        noInternetButton.setOnClickListener {
            search(STATE.info);
        }

        adapter = TrackAdapter(mutableListOf());

        tracksList.adapter = adapter;

        MusicPlayer.setOnCompleteCallback { adapter.updateTracks() }
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayer.setOnCompleteCallback(null)
    }

    private fun search(textToSearch: String) {

        itunesApi.search(textToSearch, ::onSearchSuccess, ::onSearchEmpty, ::onSearchFail)

        STATE = SEARCH_STATE.SEARCHING;

        STATE.info = textToSearch;

        MusicPlayer.destroy()
    }

    private fun setVisible(item: View) {
        val list: List<View> = listOf(tracksList, noTracks, noInternet)

        for (view in list) {
            view.visibility = if (view == item) View.VISIBLE else View.GONE;
        }
    }

    private fun restoreTracks() {
        try {
            val type = object : TypeToken<List<Track>>() {}.type;
            val tracks: List<Track> = gson.fromJson(STATE.info, type);
            setVisible(tracksList)
            for (track in tracks) {
                adapter.tracks.add(track);
            }
            adapter.notifyDataSetChanged();
        } catch (er: JsonSyntaxException) {
            search(searchText)
        }
    }

    private fun onSearchSuccess(tracks: List<ItunesTrack>) {

        setVisible(tracksList)

        adapter.tracks.clear()

        for (item in tracks) {

            val track = Track(
                item.trackName ?: "название трека",
                item.artistName ?: "имя исполнителя",
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis),
                item.artworkUrl100 ?: "",
                item.previewUrl ?: ""
            )

            adapter.tracks.add(track)
        }

        adapter.notifyDataSetChanged();

        if (STATE == SEARCH_STATE.SEARCHING) {
            tracksList.smoothScrollToPosition(0)
        }

        STATE = SEARCH_STATE.SUCCESS;
    }

    private fun onSearchEmpty() {
        setVisible(noTracks)

        STATE = SEARCH_STATE.EMPTY;
    }

    private fun onSearchFail(textToSearch: String, error: ItunesError? = null) {

        setVisible(noInternet)

        STATE = SEARCH_STATE.FAIL;

        STATE.info = textToSearch;
    }

    private fun closeKeyboard() {
        val view = this.currentFocus

        if (view != null) {

            val manager = getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_KEY_SEARCH_FIELD, searchText)
        outState.putInt(STATE_KEY_SEARCH_STATE, STATE.num)
        if (STATE == SEARCH_STATE.SUCCESS) {
            STATE.info = gson.toJson(adapter.tracks);
        }
        outState.putString(STATE_KEY_SEARCH_STATE_INFO, STATE.info)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(STATE_KEY_SEARCH_FIELD, "");
        STATE = SEARCH_STATE.find(savedInstanceState.getInt(STATE_KEY_SEARCH_STATE, 0))
        STATE.info = savedInstanceState.getString(STATE_KEY_SEARCH_STATE_INFO, "");
        val editText = findViewById<EditText>(R.id.editText)
        editText.setText(searchText)

        when (STATE) {
            SEARCH_STATE.NO_ACTION -> {}
            SEARCH_STATE.SEARCHING -> {}
            SEARCH_STATE.SUCCESS -> restoreTracks()
            SEARCH_STATE.EMPTY -> onSearchEmpty()
            SEARCH_STATE.FAIL -> onSearchFail(STATE.info)
        }
    }
}