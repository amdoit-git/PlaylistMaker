package com.example.playlistmaker

enum class SEARCH_STATE(val num:Int, var info:String = ""){
    NO_ACTION(1), SEARCHING(2),SUCCESS(3), EMPTY(4), FAIL(5);

    companion object{
        fun find(num: Int): SEARCH_STATE{
            return entries.firstOrNull { it.num == num } ?: SEARCH_STATE.NO_ACTION
        }
    }
}