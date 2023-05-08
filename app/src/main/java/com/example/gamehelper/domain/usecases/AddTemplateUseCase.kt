package com.example.gamehelper.domain.usecases

import com.example.gamehelper.domain.repository.GameHelperRepository
import com.example.gamehelper.domain.entity.Template

class AddTemplateUseCase (private val gameHelperRepository: GameHelperRepository) {
    suspend fun addTemplate(item: Template){
        gameHelperRepository.addTemplate(item)
    }
}