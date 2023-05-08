package com.example.gamehelper.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(var name: String, var score: Double) : Parcelable {
}