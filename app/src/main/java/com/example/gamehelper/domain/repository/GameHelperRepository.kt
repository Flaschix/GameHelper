package com.example.gamehelper.domain.repository

import androidx.lifecycle.LiveData
import com.example.gamehelper.domain.entity.Template
import com.example.gamehelper.domain.entity.User

interface GameHelperRepository {

    suspend fun addTemplate(item: Template)

    suspend fun editTemplate(item: Template)

    suspend fun deleteTemplate(item: Template)

    suspend fun getTemplate(userLogin: String): Template

    fun getTemplateList(): LiveData<List<Template>>

    suspend fun addUser(item: User)

    suspend fun editUser(item: User)

    suspend fun deleteUser(item: User)

    suspend fun getUser(login: String): User

    fun getUserList(): LiveData<List<User>>
}