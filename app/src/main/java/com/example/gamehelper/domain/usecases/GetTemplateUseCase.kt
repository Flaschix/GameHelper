package com.example.gamehelper.domain.usecases

import com.example.gamehelper.domain.repository.GameHelperRepository

class GetTemplateUseCase (private val gameHelperRepository: GameHelperRepository) {
    suspend fun getTemplate(userLogin: String){
        gameHelperRepository.getTemplate(userLogin)
    }
}