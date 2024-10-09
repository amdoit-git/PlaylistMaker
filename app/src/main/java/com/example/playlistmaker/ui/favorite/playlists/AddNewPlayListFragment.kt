package com.example.playlistmaker.ui.favorite.playlists

import android.R.attr
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddNewPlayListBinding
import com.example.playlistmaker.ui.common.DpToPx
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream


class AddNewPlayListFragment : Fragment(), DpToPx {

    private var _binding: FragmentAddNewPlayListBinding? = null

    private val binding get() = _binding!!

    private var cacheFile: File? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {

                val cover = binding.playlistPhoto

                //cover.setImageURI(uri)

                Glide.with(cover).load(uri).apply(
                    RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                ).transform(
                    CenterCrop(), RoundedCorners(dpToPx(8f, cover.context))
                )
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            cacheFile = null
                            cover.setBackgroundResource(R.drawable.add_photo_not_selected)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {

                            cover.setImageDrawable(resource)

                            cover.setBackgroundColor(requireContext().getColor(R.color.transparent))

                            return false
                        }

                    })
                    .into(cover)

                Glide.with(requireContext().applicationContext).download(uri).apply(RequestOptions.overrideOf(600,600)).centerCrop().listener(object : RequestListener<File> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<File>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: File,
                        model: Any,
                        target: Target<File>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val uri = Uri.fromFile(resource)
                        return false
                    }

                }).submit()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //Remove the listener before proceeding
                    binding.root.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    // measure your views here
                    switchButtonsVisibility()
                }
            })

        binding.playlistPhoto.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
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

    private fun saveImageToPrivateStorage() {

        val drawable = binding.playlistPhoto.getDrawable() as BitmapDrawable

        val dirName = "playlists_covers"

        val file = File(requireContext().getDir(dirName, MODE_PRIVATE), "x.jpg")

        try {
            val out = FileOutputStream(file)
            drawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("WWW", "file size = " + file.length())
    }

    private fun setBorderColor(layout: TextInputLayout, isBlank: Boolean) {

        if (context == null) return

        if (isBlank) {

            layout.setDefaultHintTextColor(
                ContextCompat.getColorStateList(
                    requireContext(), R.color.addNewPlaylistEditTextColor
                )
            )
            layout.setBoxStrokeColorStateList(
                ContextCompat.getColorStateList(
                    requireContext(), R.color.box_input_layout_stroke_color_empty
                )!!
            )

        } else {

            layout.setDefaultHintTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.boxStrokeColor)
            )
            layout.setBoxStrokeColorStateList(
                ContextCompat.getColorStateList(
                    requireContext(), R.color.box_input_layout_stroke_color
                )!!
            )
        }
    }

    private fun enableEditText(
        layout: TextInputLayout, elem: EditText, callback: (String) -> Unit
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