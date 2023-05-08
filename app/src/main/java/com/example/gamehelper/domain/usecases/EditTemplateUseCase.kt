package com.example.gamehelper.domain.usecases

import com.example.gamehelper.domain.repository.GameHelperRepository
import com.example.gamehelper.domain.entity.Template

class EditTemplateUseCase (private val gameHelperRepository: GameHelperRepository) {
    suspend fun editTemplate(item: Template){
        gameHelperRepository.editTemplate(item)
    }
}