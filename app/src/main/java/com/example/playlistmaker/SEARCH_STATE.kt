package com.example.playlistmaker

enum class SEARCH_STATE(val num:Int, var jsonTracks:String = ""){
    FIRST_VISIT(1),//пустой экран
    HISTORY_TRACKS_VISIBLE(2),//история прослушивания на экране и ее ведно
    HISTORY_GONE(3),//история прослушивания на экране, но она скрыта
    HISTORY_EMPTY(4),//на экране история прослушивания в которой нет треков == (пустой экран)
    SEARCH_TRACKS_VISIBLE(5),//на экране список треков найденный в поиске
    SEARCH_EMPTY(6),//ошибка поиска "ничего ненайдено"
    SEARCH_FAIL(7);//ошибка поиска "проблемы с сетью, повторите запрос"

    companion object{
        fun find(num: Int): SEARCH_STATE{
            return entries.firstOrNull { it.num == num } ?: SEARCH_STATE.FIRST_VISIT
        }
    }
}