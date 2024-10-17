package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddNewPlayListBinding
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.AddNewPlayListViewModel
import com.example.playlistmaker.viewModels.mediaLibrary.playlists.NewPlaylistTabData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.min


class AddNewPlayListFragment : Fragment(), DpToPx {

    private val vModel: AddNewPlayListViewModel by viewModel()

    private var _binding: FragmentAddNewPlayListBinding? = null

    private val binding get() = _binding!!

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia(), ::saveCoverToTmpStorage
    )

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
                    calcButtonPosition()
                }
            })

        binding.cover.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.backButton.setOnClickListener {
            vModel.onBackPressed()
        }

        binding.fixedButton.setOnClickListener {
            vModel.onCreateButtonPressed()
        }

        binding.scrolledButton.setOnClickListener {
            vModel.onCreateButtonPressed()
        }

        vModel.getLiveData().observe(viewLifecycleOwner) {

            when (it) {
                is NewPlaylistTabData.Button -> buttonsSetEnabled(it.enabled)
                is NewPlaylistTabData.Cover -> setCoverImage(it.uri)
                is NewPlaylistTabData.Description -> {
                    binding.etPlaylistDescription.setText(it.text)
                    calcButtonPosition()
                }

                is NewPlaylistTabData.Title -> {
                    binding.etPlaylistTitle.setText(it.text)
                    calcButtonPosition()
                }

                is NewPlaylistTabData.Close -> {
                    if (it.allowed) {
                        goBack()
                    } else {
                        MaterialAlertDialogBuilder(
                            requireContext(), R.style.AlertDialogTheme
                        ).setTitle(R.string.play_list_go_back_title)
                            .setMessage(R.string.play_list_go_back_description)
                            .setNegativeButton(R.string.play_list_go_back_cancel) { _, _ ->
                                //Остаемся на экране создания плейлиста
                            }.setPositiveButton(R.string.play_list_go_back_confirm) { _, _ ->
                                goBack()
                            }.show()
                    }
                }
            }
        }

        enableEditText(binding.playlistTitle, binding.etPlaylistTitle) { text ->
            vModel.onTitleChanged(text)
        }

        enableEditText(binding.playlistDescription, binding.etPlaylistDescription) { text ->
            vModel.onDescriptionChanged(text)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(
            enabled = true
        ) {
            override fun handleOnBackPressed() {
                vModel.onBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun saveCoverToTmpStorage(uri: Uri?) {

        if (uri != null) {

            val cover = binding.cover
            val size = getMinSize()

            Glide.with(cover).asBitmap().load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(size, size)
                .transform(
                    CenterCrop()
                ).listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean
                    ): Boolean {
                        vModel.showMessage(
                            R.string.play_list_image_error
                        )
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        vModel.saveCoverToTmpStorage(resource, uri)
                        return false
                    }

                }).submit()
        }
    }

    private fun setCoverImage(uri: Uri?) {

        val cover = binding.cover

        if (uri != null) {
            Glide.with(cover).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(
                    CenterCrop(), RoundedCorners(dpToPx(8f, cover.context))
                ).into(cover)

            cover.setBackgroundColor(requireContext().getColor(R.color.transparent))

        } else {

            Glide.with(binding.cover).clear(binding.cover)

            cover.setBackgroundResource(R.drawable.add_photo_not_selected)
        }
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
        layout: TextInputLayout, elem: TextInputEditText, callback: (String) -> Unit
    ) {
        elem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val text = elem.text.toString()

                setBorderColor(layout, text.isBlank())

                calcButtonPosition()

                callback(elem.text.toString())
            }
        })
    }

    private fun buttonsSetEnabled(isEnabled: Boolean) {
        binding.fixedButton.isEnabled = isEnabled
        binding.scrolledButton.isEnabled = isEnabled
    }

    private fun getMinSize(): Int {
        return min(
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.heightPixels
        )
    }

    private fun getYPosition(elem: View): Int {
        val xy = intArrayOf(0, 0)
        elem.getLocationOnScreen(xy)
        return xy[1]
    }

    private fun calcButtonPosition() {

        val fixed = binding.fixedButton
        val scrolled = binding.scrolledButton

        if (getYPosition(fixed) > getYPosition(scrolled)) {
            fixed.visibility = View.VISIBLE
            scrolled.visibility = View.INVISIBLE
        } else {
            fixed.visibility = View.INVISIBLE
            scrolled.visibility = View.VISIBLE
        }
    }
}