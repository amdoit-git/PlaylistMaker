package com.example.playlistmaker.ui.common

interface NumDeclension {
    fun declension(num: Int, str: String): String {
        val result: String
        var count: Int = num % 100
        val expressions = str.split(" ")
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

        return "$num $result"
    }
}