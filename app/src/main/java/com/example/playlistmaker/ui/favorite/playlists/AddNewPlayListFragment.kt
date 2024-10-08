package com.example.playlistmaker.ui.favorite.playlists

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddNewPlayListBinding
import com.google.android.material.textfield.TextInputLayout

class AddNewPlayListFragment : Fragment() {

    private var _binding: FragmentAddNewPlayListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.getViewTreeObserver().addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //Remove the listener before proceeding
                    binding.root.getViewTreeObserver().removeOnGlobalLayoutListener(this)

                    // measure your views here

                    switchButtonsVisibility()
                }
            }
        )
    }

    override fun onResume() {

        super.onResume()

        enableEditText(binding.playlistName, binding.etPlaylistName) { text ->

            buttonsSetEnabled(text.isNotEmpty())
        }

        enableEditText(binding.playlistDescription, binding.etPlaylistDescription) { text ->

        }


    }

    override fun onStop() {
        disableEditText(binding.etPlaylistName)
        disableEditText(binding.etPlaylistDescription)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBorderColor(layout: TextInputLayout, isBlank: Boolean) {

        if (isBlank) {
            layout.setDefaultHintTextColor(ContextCompat.getColorStateList(requireContext(), R.color.text_input_layout_stroke_color)!!)
            layout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.box_input_layout_stroke_color_empty)!!)
        } else {
            layout.setDefaultHintTextColor(ColorStateList.valueOf(resources.getColor(R.color.boxStrokeColor)))
            layout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(requireContext(), R.color.box_input_layout_stroke_color)!!)
        }
    }

    private fun enableEditText(
        layout: TextInputLayout,
        elem: EditText,
        callback: (String) -> Unit
    ) {

        elem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val text = elem.text.toString()

                setBorderColor(layout, text.isBlank())

                switchButtonsVisibility()

                callback(elem.text.toString())
            }
        })
    }

    private fun disableEditText(elem: EditText) {
        elem.addTextChangedListener(null)
    }

    private fun buttonsSetEnabled(isEnabled: Boolean) {
        binding.fixedButton.isEnabled = isEnabled
        binding.scrolledButton.isEnabled = isEnabled
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    private fun switchButtonsVisibility() {

        val fixed = binding.fixedButton
        val scrolled = binding.scrolledButton

        Log.d("WWW", "fixed = ${getYPosition(fixed)}, scrolled = ${getYPosition(scrolled)}")

        if (getYPosition(fixed) > getYPosition(scrolled)) {
            fixed.visibility = View.VISIBLE
            scrolled.visibility = View.INVISIBLE
        } else {
            fixed.visibility = View.INVISIBLE
            scrolled.visibility = View.VISIBLE
        }
    }
}