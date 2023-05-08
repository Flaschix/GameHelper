package com.example.gamehelper.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
  val game_name: String,
  val game_count: Int,
  val avg_time: Int,
  val bestScore: Double,
  val faster_game: Int,
  val slower_game: Int
) : Parcelable