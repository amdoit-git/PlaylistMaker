package com.example.playlistmaker.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.viewModels.search.SearchData
import com.example.playlistmaker.viewModels.search.SearchViewModel
import com.example.playlistmaker.viewModels.search.TrackListState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchFragment : Fragment() {

    private lateinit var tracksList: RecyclerView

    private val vModel: SearchViewModel by viewModel()

    private val adapter: TrackAdapter by inject {
        parametersOf(vModel::onTrackClicked, vModel::clearHistory, ::scrollListToTop)
    }

    private var _binding: ActivitySearchBinding? = null

    private val binding get() = _binding!!

    private var isSearchActive = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracksList = binding.tracksList

        tracksList.adapter = adapter

        binding.noInternetButton.setOnClickListener {
            vModel.searchOnITunes()
        }

        vModel.getLiveData().observe(viewLifecycleOwner) {

            when (it) {

                is SearchData.ProgressBar -> {
                    binding.progressBar.isVisible = it.visible
                }

                is SearchData.SearchText -> {
                    binding.editText.setText(it.text)
                    binding.editTextDelete.isVisible = it.text.isNotEmpty()
                    if (it.textInFocus) {
                        binding.editText.requestFocus()
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

                is SearchData.OpenPlayerScreen -> {

                    val direction =
                        SearchFragmentDirections.actionSearchFragmentToPlayerScreenFragment(it.track)

                    findNavController().navigate(direction)
                }

                is SearchData.ScrollTracksList -> {
                    scrollTracksList(it.position)
                }
            }
        }

        initTextField(binding.editText)
    }

    override fun onResume() {
        super.onResume()
        isSearchActive = true
    }

    override fun onStop() {
        super.onStop()
        isSearchActive = false
    }

    private fun scrollListToTop() {
        tracksList.scrollToPosition(0)
    }

    private fun showTracksList(tracks: List<Track>, showClearButton: Boolean = false) {

        adapter.setNewTracksList(tracks)

        adapter.showClearButton(showClearButton)

        adapter.notifyDataSetChanged()
    }

    private fun scrollTracksList(position: Int) {
        tracksList.smoothScrollToPosition(position)
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

                if (isSearchActive) {

                    val text = editText.text.toString()

                    binding.editTextDelete.isVisible = text.isNotEmpty()

                    vModel.onTextChanged(text)
                }
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

        if (editText.hasFocus()) {
            vModel.onFocusChanged(true)
        }
    }

    private fun disableTextField(editText: EditText) {

        editText.addTextChangedListener(null)

        editText.setOnEditorActionListener(null)

        editText.onFocusChangeListener = null

        binding.editTextDelete.setOnClickListener(null)
    }

    private fun closeKeyboard() {

        val view = activity?.currentFocus

        if (activity != null && view != null) {

            val manager = requireActivity().baseContext.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager.hideSoftInputFromWindow(
                view.windowToken, 0
            )
        }
    }
}