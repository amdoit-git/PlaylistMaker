package com.example.playlistmaker.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.viewModels.main.MainActivityData
import com.example.playlistmaker.viewModels.main.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity() : AppCompatActivity() {

    private val vModel: MainActivityViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragments_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerScreenFragment -> {
                    hideBottomNavigation()
                }

                R.id.addNewPlayListFragment -> {
                    hideBottomNavigation()
                }

                R.id.playlistScreenFragment -> {
                    hideBottomNavigation()
                }

                else -> {
                    showBottomNavigation()
                }
            }
        }

        vModel.getLiveData().observe(this){

            when(it){
                is MainActivityData.ToastMessage -> {
                    binding.infoText.text = it.message

                    binding.info.isVisible = it.isVisible
                }
            }
        }
    }

    private fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.bottomNavigationBorder.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
        binding.bottomNavigationBorder.visibility = View.GONE
    }
}