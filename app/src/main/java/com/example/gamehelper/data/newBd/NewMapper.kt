package com.example.gamehelper.data.newBd

import com.example.gamehelper.domain.entity.User

class NewMapper {
    fun mapUserToUserDbModel(user: User) = UserDbModel(
        user.login,
        user.email,
        user.password
    )

    fun mapUserDbModelToUser(userDbModel: UserDbModel) = User(
        userDbModel.login,
        userDbModel.mail,
        userDbModel.password,
    )

    fun mapListUserDbModelToListUser(list: List<UserDbModel>) = list.map {
        mapUserDbModelToUser(it)
    }
}