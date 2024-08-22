package com.example.playlistmaker.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
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

    private var fieldIsInitialized = false

    private var textInFocus = false

    private val vModel: SearchViewModel by viewModel()

    private val adapter: TrackAdapter by inject {
        parametersOf(vModel::onTrackClicked, vModel::clearHistory, ::scrollListToTop)
    }

    private var _binding: ActivitySearchBinding? = null

    private val binding get() = _binding!!

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
        Log.d("WWW", "onDestroyView()")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldIsInitialized = false

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
                    textInFocus = it.textInFocus

                    Log.d("WWW", "text = ${it.text}, textInFocus = ${it.textInFocus}")
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

                    val direction = SearchFragmentDirections.actionSearchFragmentToPlayerScreenFragment(it.track)

                    findNavController().navigate(direction)

                    //isNewBinding = false
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

        if(fieldIsInitialized){
            return
        }

        fieldIsInitialized = true

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

        if(textInFocus){
            binding.editText.requestFocus()
        }

        //binding.editText.requestFocus()
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