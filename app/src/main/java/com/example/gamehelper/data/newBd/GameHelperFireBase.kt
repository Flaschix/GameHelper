package com.example.gamehelper.data.newBd

import androidx.lifecycle.LiveData

interface GameHelperFireBase {

    fun getUserList(): LiveData<List<UserDbModel>>

    suspend fun addUser(userDbModel: UserDbModel)

    suspend fun deleteUser(userLogin: Int)

    suspend fun getUser(userLogin: Int): UserDbModel
}