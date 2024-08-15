package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.EmailData
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.viewModels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val vModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

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