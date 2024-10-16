package com.example.playlistmaker.domain.repository.common

class GetStringResourceUseCase(private val repository: GetStringRepository) {

    operator fun invoke(id: Int): String {
        return repository.get(id)
    }
}