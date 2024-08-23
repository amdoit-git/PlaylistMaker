package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.viewModels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: ActivitySettingsBinding? = null
    private val vModel: SettingsViewModel by viewModel()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivitySettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShare.setOnClickListener {
            vModel.share(
                text = getString(R.string.settings_share_text)
            )
        }

        binding.buttonSupportEmail.setOnClickListener {
            vModel.sendEmail(
                EmailData(
                    email = getString(R.string.settings_email_address),
                    subject = getString(R.string.settings_email_subject),
                    text = getString(R.string.settings_email_text)
                )
            )
        }

        binding.buttonUserAgreement.setOnClickListener {
            vModel.openUrl(
                url = getString(R.string.settings_user_agreement_url)
            )
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (switcher.isPressed) {
                vModel.switchTheme(checked)
            }
        }
    }
}