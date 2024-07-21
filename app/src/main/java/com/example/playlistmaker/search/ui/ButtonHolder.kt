package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.ClearSearchHistoryButtonBinding

class ButtonHolder(private val binding: ClearSearchHistoryButtonBinding) :
    SearchActivityHolder(binding.root) {

    override fun onButtonClick(callback: () -> Unit) {

        binding.clearSearchHistroyButton.setOnClickListener {
            callback()
        }
    }

    companion object {

        fun create(parent: ViewGroup): ButtonHolder {
            return ButtonHolder(
                ClearSearchHistoryButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}