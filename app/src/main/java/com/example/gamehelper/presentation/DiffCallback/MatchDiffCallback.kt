package com.example.gamehelper.presentation.DiffCallback

import androidx.recyclerview.widget.DiffUtil
import com.example.gamehelper.domain.entity.Match

class MatchDiffCallback: DiffUtil.ItemCallback<Match>() {
    override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem == newItem
    }
}