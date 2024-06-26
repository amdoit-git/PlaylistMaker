package com.example.playlistmaker

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SearchTextField(
    private val activity: AppCompatActivity,
    private val editText: EditText,
    private val clearButton: View,
    private val onTextChanged: (String, Boolean) -> Unit,
    private val onAction: (String) -> Unit,
    private val onFocusChanged: (String, Boolean) -> Unit
) {

    var textValue: String = ""
    var inFocus: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    fun activate() {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                clearButton.visibility = if (editText.text.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                textValue = editText.text.toString()

                onTextChanged(textValue, inFocus)

                handler.removeCallbacksAndMessages(null)

                if(textValue.isNotBlank()){
                    handler.postDelayed({
                        onAction(textValue)
                    }, 2000)
                }
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onAction(textValue)
                closeKeyboard()
                handler.removeCallbacksAndMessages(null)
                true
            } else {
                false
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            inFocus = hasFocus
            onFocusChanged(textValue, inFocus)
        }

        clearButton.setOnClickListener {
            editText.setText("")
            closeKeyboard()
        }
    }

    fun requestFocus() {
        editText.requestFocus()
    }

    fun setText(text: String) {
        this.textValue = text
        editText.setText(text)
    }

    private fun closeKeyboard() {

        val view = activity.currentFocus

        if (view != null) {

            val manager = activity.baseContext.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager.hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}