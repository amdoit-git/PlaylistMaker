package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.presentation.SearchData
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.presentation.TRACK_LIST_STATE

class SearchActivity : AppCompatActivity() {

    private lateinit var tracksList: RecyclerView
    private lateinit var adapter: TrackAdapter

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.Factory(application)
        )[SearchViewModel::class.java]

        tracksList = binding.tracksList

        adapter =
            TrackAdapter(viewModel::onTrackClicked, viewModel::clearHistory, ::scrollListToTop)

        tracksList.adapter = adapter

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

        binding.noInternetButton.setOnClickListener {
            viewModel.searchOnITunes()
        }

        viewModel.getLiveData().observe(this) {

            when (it) {

                is SearchData.ProgressBar -> {
                    binding.progressBar.visibility = if (it.visible) View.VISIBLE else View.GONE
                }

                is SearchData.SearchText -> {
                    binding.editText.setText(it.text)
                    binding.editTextDelete.visibility = if (it.text.isEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }

                is SearchData.TrackList -> {

                    val STATE = it.state

                    val elements: List<View> = listOf(
                        tracksList,
                        binding.noTracks,
                        binding.noInternet,
                        binding.tracksListTitle
                    )

                    elements.forEach { view ->
                        view.visibility = View.GONE
                    }

                    when (STATE) {
                        TRACK_LIST_STATE.FIRST_VISIT -> {}
                        TRACK_LIST_STATE.HISTORY_EMPTY -> {}
                        TRACK_LIST_STATE.HISTORY_GONE -> {}
                        TRACK_LIST_STATE.HISTORY_VISIBLE -> {
                            binding.tracksListTitle.visibility = View.VISIBLE
                            tracksList.visibility = View.VISIBLE
                        }

                        TRACK_LIST_STATE.SEARCH_VISIBLE -> {
                            tracksList.visibility = View.VISIBLE
                        }

                        TRACK_LIST_STATE.SEARCH_EMPTY -> {
                            binding.noTracks.visibility = View.VISIBLE
                        }

                        TRACK_LIST_STATE.SEARCH_FAIL -> {
                            binding.noInternet.visibility = View.VISIBLE
                        }
                    }

                    STATE.tracks?.let { tracks ->
                        showTracksList(tracks, STATE == TRACK_LIST_STATE.HISTORY_VISIBLE)
                    } ?: run {
                        clearTracksList()
                    }
                }

                is SearchData.MoveToTop -> {
                    adapter.moveTrackToTop(it.track)
                }
            }
        }

        initTextField(binding.editText)
    }

    private fun scrollListToTop() {
        tracksList.scrollToPosition(0)
    }

    private fun showTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.tracks.clear()

        tracks.forEach {
            adapter.tracks.add(it)
        }

        adapter.showClearButton(showClearButton)

        adapter.notifyDataSetChanged()

        tracksList.smoothScrollToPosition(0)
    }

    private fun clearTracksList() {
        adapter.tracks.clear()
        adapter.showClearButton(false)
        adapter.notifyDataSetChanged()
    }

    private fun initTextField(editText: EditText) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                val text = editText.text.toString()

                binding.editTextDelete.visibility = if (text.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                viewModel.onTextChanged(text)
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onActionButton()
                closeKeyboard()
                true
            } else {
                false
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus)
        }

        binding.editTextDelete.setOnClickListener {
            editText.setText("")
            closeKeyboard()
        }
    }

    private fun closeKeyboard() {

        val view = currentFocus

        if (view != null) {

            val manager = baseContext.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager.hideSoftInputFromWindow(
                view.windowToken, 0
            )
        }
    }
}