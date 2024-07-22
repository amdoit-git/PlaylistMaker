package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.EmailData
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.Factory(application)
        )[SettingsViewModel::class.java]

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

        binding.buttonShare.setOnClickListener {
            viewModel.share(
                text = getString(R.string.settings_share_text)
            )
        }

        binding.buttonSupportEmail.setOnClickListener {
            viewModel.sendEmail(
                EmailData(
                    email = getString(R.string.settings_email_address),
                    subject = getString(R.string.settings_email_subject),
                    text = getString(R.string.settings_email_text)
                )
            )
        }

        binding.buttonUserAgreement.setOnClickListener {
            viewModel.openUrl(
                url = getString(R.string.settings_user_agreement_url)
            )
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (switcher.isPressed) {
                viewModel.switchTheme(checked)
            }
        }
    }
}