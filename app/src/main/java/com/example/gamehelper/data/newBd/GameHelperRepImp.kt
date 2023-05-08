package com.example.gamehelper.data.newBd

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.gamehelper.domain.entity.Template
import com.example.gamehelper.domain.entity.User
import com.example.gamehelper.domain.repository.GameHelperRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class GameHelperRepImp(application: Application): GameHelperRepository {
    private val gameHelperFire = AppDatabase.getInstance(application)
    private val mapper = NewMapper()

    override suspend fun addTemplate(item: Template) {
        TODO("Not yet implemented")
    }

    override suspend fun editTemplate(item: Template) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTemplate(item: Template) {
        TODO("Not yet implemented")
    }

    override suspend fun getTemplate(templateUserLogin: String): Template {
        TODO("Not yet implemented")
    }

    override fun getTemplateList(): LiveData<List<Template>> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(item: User) {
        TODO("Not yet implemented")
    }

    override suspend fun editUser(item: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(item: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(login: String): User {
        TODO("Not yet implemented")
    }

    override fun getUserList(): LiveData<List<User>> {
        TODO("Not yet implemented")
    }

    companion object{
        private val TABLE_USERS = "Users"
        private val MAIL = "mail"
        private val PASSWORD = "password"
    }
}