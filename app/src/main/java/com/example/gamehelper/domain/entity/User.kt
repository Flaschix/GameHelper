package com.example.gamehelper.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val login: String, val email: String, val password: String) : Parcelable {
}