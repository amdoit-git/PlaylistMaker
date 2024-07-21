package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.common.domain.models.Track

//Состояние нашего списка треков

enum class TRACK_LIST_STATE(val num: Int, var tracks: List<Track>? = null) {
    FIRST_VISIT(1),//пустой экран
    HISTORY_VISIBLE(2),//история прослушивания на экране и ее ведно
    HISTORY_GONE(3),//история прослушивания на экране, но она скрыта
    HISTORY_EMPTY(4),//на экране история прослушивания в которой нет треков == (пустой экран)
    SEARCH_VISIBLE(5),//на экране список треков найденный в поиске
    SEARCH_EMPTY(6),//ошибка поиска "ничего ненайдено"
    SEARCH_FAIL(7);//ошибка поиска "проблемы с сетью, повторите запрос"
}