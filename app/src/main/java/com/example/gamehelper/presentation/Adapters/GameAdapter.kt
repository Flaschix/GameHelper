package com.example.gamehelper.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.gamehelper.R
import com.example.gamehelper.domain.entity.Game
import com.example.gamehelper.presentation.DiffCallback.GameDiffCallback
import com.example.gamehelper.presentation.ViewHolders.GameViewHolder

class GameAdapter: ListAdapter<Game, GameViewHolder>(GameDiffCallback()) {

    var onGameClickListener: ((Game) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val layout = R.layout.match_item
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)

        holder.view.setOnClickListener {
            onGameClickListener?.invoke(game)
        }

        holder.name.text = game.game_name

    }

    companion object{
        const val MAX_POOL_SIZE = 30
    }
}