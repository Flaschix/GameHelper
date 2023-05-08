package com.example.gamehelper.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Template (
    var user_login: String,
    var name: String,
    var playerCount: Int,
    var dice: Boolean,
    var timer: Boolean,
    var liners: Boolean,
    var table: Boolean
    ) : Parcelable {

    companion object{
        const val UNDEFINED_ID = 0
    }
}