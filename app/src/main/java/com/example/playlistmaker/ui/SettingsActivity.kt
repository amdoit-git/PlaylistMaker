package com.example.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repository.AppSettingsRepositoryImpl
import com.example.playlistmaker.domain.models.APP_THEME
import com.example.playlistmaker.domain.usecase.AppThemeSaverInteractorImpl
import com.example.playlistmaker.domain.usecase.SetAppThemeUseCase
import com.example.playlistmaker.ui.repository.SetAppThemeRepositoryImpl
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val repository by lazy { AppSettingsRepositoryImpl(context = this) }
    private val appThemeSaver by lazy { AppThemeSaverInteractorImpl(repository = repository) }
    private val setTheme by lazy {SetAppThemeUseCase(SetAppThemeRepositoryImpl())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val b_to_main_screen = findViewById<View>(R.id.button_to_main_screen)

        b_to_main_screen.setOnClickListener {
            this.finish()
        }

        val b_share = findViewById<View>(R.id.button_share)

        b_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text))
            intent.type = "text/plain"
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
            //startActivity(intent)
        }

        val b_support = findViewById<View>(R.id.button_support_email)

        b_support.setOnClickListener {
            var email: String = getString(R.string.settings_email_address)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_email_text))
            startActivity(intent)
        }

        val b_user_agreement = findViewById<View>(R.id.button_user_agreement)

        b_user_agreement.setOnClickListener {
            var url: String = getString(R.string.settings_user_agreement_url)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            val theme = APP_THEME.find(checked)
            setTheme.execute(theme)
            appThemeSaver.saveTheme(theme)
        }
    }
}