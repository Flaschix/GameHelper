package com.example.gamehelper.presentation.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehelper.R

class MatchViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val title = view.findViewById<TextView>(R.id.tvName)
}