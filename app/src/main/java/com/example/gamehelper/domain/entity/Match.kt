package com.example.gamehelper.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Match(
    var user_login: String,
    var name: String,
    var T: Int,
    var winner: String,
    var listOfPlayers: List<Player>,
    var id: String,
    var title: String
) : Parcelable {

    companion object{
        const val UNDEFINED_ID = 0
    }
}