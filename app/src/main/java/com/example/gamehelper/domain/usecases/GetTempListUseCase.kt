package com.example.gamehelper.domain.usecases

import androidx.lifecycle.LiveData
import com.example.gamehelper.domain.repository.GameHelperRepository
import com.example.gamehelper.domain.entity.Template

class GetTempListUseCase (private val gameHelperRepository: GameHelperRepository) {
    fun getTemplateList(): LiveData<List<Template>>{
        return gameHelperRepository.getTemplateList()
    }
}