package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.FakeTracks
import com.example.testapp.Track


class SearchActivity : AppCompatActivity() {

    val SEARCH_FIELD_STATE_KEY = "SEARCH_FIELD_STATE_KEY";
    private var searchText: String = "";
    private lateinit var adapter: TrackAdapter;
    private var tracks: MutableList<Track> = mutableListOf();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val b_to_main_screen = findViewById<View>(R.id.button_to_main_screen)
        val editText_delete = findViewById<View>(R.id.editText_delete)
        val editText = findViewById<EditText>(R.id.editText)

        b_to_main_screen.setOnClickListener({
            this.finish();
        })

        editText_delete.setOnClickListener({
            editText.setText("")
            closeKeyboard()
        })

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

        val tracksList = findViewById<RecyclerView>(R.id.tracksList)

        FakeTracks.setZero()

        for(i in 0..30) {
            tracks.add(FakeTracks.getTrack());
        }

        adapter = TrackAdapter(tracks);

        tracksList.adapter = adapter;
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
        outState.putString(SEARCH_FIELD_STATE_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_FIELD_STATE_KEY, "");
        val editText = findViewById<EditText>(R.id.editText)
        editText.setText(searchText)
    }
}