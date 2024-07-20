package com.example.playlistmaker.common.domain.consumer

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val code:Int, val message: String) : ConsumerData<T>
}
