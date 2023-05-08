package com.example.gamehelper.domain.usecases

import com.example.gamehelper.domain.repository.GameHelperRepository
import com.example.gamehelper.domain.entity.Template

class DeleteTemplateUseCase(private val gameHelperRepository: GameHelperRepository) {
    suspend fun deleteTemplate(item: Template){
        gameHelperRepository.deleteTemplate(item)
    }
}
