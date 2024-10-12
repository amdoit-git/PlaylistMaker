package com.example.playlistmaker.ui.favorite.playlists

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddNewPlayListBinding
import com.example.playlistmaker.ui.common.DpToPx
import com.example.playlistmaker.viewModels.favorite.playlists.AddNewPlayListViewModel
import com.example.playlistmaker.viewModels.favorite.playlists.NewPlaylistData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


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

        binding.backButton.setOnClickListener{
            vModel.onBackPressed()
        }

        binding.fixedButton.setOnClickListener{
            vModel.onCreateButtonPressed()
        }

        binding.scrolledButton.setOnClickListener{
            vModel.onCreateButtonPressed()
        }

        vModel.getLiveData().observe(viewLifecycleOwner) {

            when (it) {
                is NewPlaylistData.Button -> buttonsSetEnabled(it.enabled)
                is NewPlaylistData.Cover -> setCoverImage(it.uri)
                is NewPlaylistData.Description -> {
                    binding.etPlaylistDescription.setText(it.text)
                    calcButtonPosition()
                }

                is NewPlaylistData.Title -> {
                    binding.etPlaylistTitle.setText(it.text)
                    calcButtonPosition()
                }

                is NewPlaylistData.Close -> {
                    if (it.allowed) {
                        goBack()
                    } else {
                        MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.play_list_go_back_title)
                            .setMessage(R.string.play_list_go_back_description)
                            .setNegativeButton(R.string.play_list_go_back_cancel) { _, _ ->
                                // Действия, выполняемые при нажатии на кнопку «Нет»
                            }.setPositiveButton(R.string.play_list_go_back_confirm) { _, _ ->
                                goBack()
                            }.show()
                    }
                }
            }
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

    override fun onResume() {

        super.onResume()

        enableEditText(binding.playlistTitle, binding.etPlaylistTitle) { text ->
            vModel.onTitleChanged(text)
        }

        enableEditText(binding.playlistDescription, binding.etPlaylistDescription) { text ->
            vModel.onDescriptionChanged(text)
        }
    }

    override fun onStop() {
        disableEditText(binding.etPlaylistTitle)
        disableEditText(binding.etPlaylistDescription)
        super.onStop()
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

            Glide.with(binding.cover).asBitmap().load(uri).centerCrop()
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean
                    ): Boolean {
                        vModel.saveCoverToTmpStorage(null)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        vModel.saveCoverToTmpStorage(resource)
                        return false
                    }

                }).submit()
        }

        //.into(cover)
    }

    private fun setCoverImage(uri: Uri?) {

        val cover = binding.cover

        if (uri != null) {
            Glide.with(cover).load(uri).transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, cover.context))
            ).into(cover)

            cover.setBackgroundColor(requireContext().getColor(R.color.transparent))
        } else {
            cover.setBackgroundResource(R.drawable.add_photo_not_selected)
        }
    }

    private fun saveImageToPrivateStorage() {

        val drawable = binding.cover.getDrawable() as BitmapDrawable

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