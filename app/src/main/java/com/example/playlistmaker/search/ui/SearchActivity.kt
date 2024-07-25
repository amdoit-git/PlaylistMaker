package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.presentation.SearchData
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.presentation.TrackListState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchActivity : AppCompatActivity() {

    private lateinit var tracksList: RecyclerView

    private var fieldIsInitialized = false

    private val vModel: SearchViewModel by viewModel()

    private val adapter: TrackAdapter by inject {
        parametersOf(vModel::onTrackClicked, vModel::clearHistory, ::scrollListToTop)
    }

    private val binding: ActivitySearchBinding by inject {
        parametersOf(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        tracksList = binding.tracksList

        tracksList.adapter = adapter

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

        binding.noInternetButton.setOnClickListener {
            vModel.searchOnITunes()
        }

        vModel.getLiveData().observe(this) {

            when (it) {

                is SearchData.ProgressBar -> {
                    binding.progressBar.isVisible = it.visible
                }

                is SearchData.SearchText -> {
                    binding.editText.setText(it.text)
                    binding.editTextDelete.isVisible = it.text.isNotEmpty()
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
                        TrackListState.FIRST_VISIT -> {}
                        TrackListState.HISTORY_EMPTY -> {}
                        TrackListState.HISTORY_GONE -> {}
                        TrackListState.HISTORY_VISIBLE -> {
                            binding.tracksListTitle.visibility = View.VISIBLE
                            tracksList.visibility = View.VISIBLE
                        }

                        TrackListState.SEARCH_VISIBLE -> {
                            tracksList.visibility = View.VISIBLE
                        }

                        TrackListState.SEARCH_EMPTY -> {
                            binding.noTracks.visibility = View.VISIBLE
                        }

                        TrackListState.SEARCH_FAIL -> {
                            binding.noInternet.visibility = View.VISIBLE
                        }
                    }

                    STATE.tracks?.let { tracks ->
                        showTracksList(tracks, STATE == TrackListState.HISTORY_VISIBLE)
                    } ?: run {
                        clearTracksList()
                    }
                }

                is SearchData.MoveToTop -> {
                    adapter.moveTrackToTop(it.track)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initTextField(binding.editText)
    }

    private fun scrollListToTop() {
        tracksList.scrollToPosition(0)
    }

    private fun showTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.setNewTracksList(tracks)

        adapter.showClearButton(showClearButton)

        adapter.notifyDataSetChanged()

        tracksList.smoothScrollToPosition(0)
    }

    private fun clearTracksList() {
        adapter.clearTracks()
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

                binding.editTextDelete.isVisible = text.isNotEmpty()

                vModel.onTextChanged(text)
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                vModel.onActionButton()
                closeKeyboard()
                true
            } else {
                false
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            vModel.onFocusChanged(hasFocus)
        }

        binding.editTextDelete.setOnClickListener {
            editText.setText("")
            closeKeyboard()
        }

        fieldIsInitialized = true
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