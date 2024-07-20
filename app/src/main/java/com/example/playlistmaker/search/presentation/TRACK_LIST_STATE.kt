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

    companion object {

        private var tracks: List<Track>? = null

        fun find(num: Int): TRACK_LIST_STATE {
            return entries.firstOrNull { it.num == num } ?: FIRST_VISIT
        }

        fun changeState(newSTATE: TRACK_LIST_STATE, oldState: TRACK_LIST_STATE): TRACK_LIST_STATE {

            if (oldState == HISTORY_VISIBLE) {
                //при смене состояния мы не очищаем список на экране, а лишь скрываем его
                this.tracks = oldState.tracks
            }

            if (newSTATE == SEARCH_VISIBLE) {
                //заполнили список на экране треками с поиска убрав историю прослушивания
                clear()
            }

            if (newSTATE == HISTORY_VISIBLE && this.tracks != null) {
                //если не были показаны треки с iTunes, то в списке на экране находятся треки с истории поиска
                newSTATE.tracks = this.tracks
            }

            return newSTATE
        }

        fun clear() {
            this.tracks = null
        }
    }
}