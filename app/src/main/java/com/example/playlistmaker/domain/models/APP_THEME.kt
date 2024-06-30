package com.example.playlistmaker.domain.models

enum class APP_THEME(val num: Boolean?) {
    DEFAULT(null), LIGHT(false), DARK(true);

    companion object {
        fun find(num: Boolean?): APP_THEME {
            return APP_THEME.entries.firstOrNull { it.num === num } ?: DEFAULT
        }
    }
}
