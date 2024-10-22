package com.example.playlistmaker.viewModels.common

import java.util.Locale

interface NumDeclension {

    fun declension(num: Int, str: String): String {
        val result: String
        var count: Int = num % 100
        val expressions = str.split(" ")

        if (Locale.getDefault().language.lowercase() == "ru") {

            if (count in 5..20) {
                result = expressions[2]
            } else {
                count %= 10
                result = when (count) {
                    1 -> expressions[0]
                    in 2..4 -> expressions[1]
                    else -> expressions[2]
                }
            }
        } else {

            result = if (num == 1) {
                expressions[0]
            } else {
                expressions[1]
            }
        }

        return "$num $result"
    }
}