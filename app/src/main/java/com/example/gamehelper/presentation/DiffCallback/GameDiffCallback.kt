package com.example.gamehelper.presentation.DiffCallback

import androidx.recyclerview.widget.DiffUtil
import com.example.gamehelper.domain.entity.Game

class GameDiffCallback: DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.game_name == newItem.game_name
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}