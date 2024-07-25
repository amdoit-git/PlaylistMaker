package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.EmailData
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class SettingsActivity : AppCompatActivity() {

    private val binding: ActivitySettingsBinding by inject{
        parametersOf(layoutInflater)
    }
    private val vModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.buttonToMainScreen.setOnClickListener {
            this.finish()
        }

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