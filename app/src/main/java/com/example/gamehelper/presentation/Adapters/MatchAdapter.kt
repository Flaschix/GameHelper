package com.example.gamehelper.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.gamehelper.R
import com.example.gamehelper.domain.entity.Match
import com.example.gamehelper.presentation.DiffCallback.MatchDiffCallback
import com.example.gamehelper.presentation.ViewHolders.MatchViewHolder

class MatchAdapter: ListAdapter<Match, MatchViewHolder>(MatchDiffCallback()) {

    var onMatchClickListener: ((Match) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val layout = R.layout.match_item
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = getItem(position)

        holder.view.setOnClickListener {
            onMatchClickListener?.invoke(match)
        }

        holder.title.text = match.title

    }

    companion object{
        const val MAX_POOL_SIZE = 30
    }
}