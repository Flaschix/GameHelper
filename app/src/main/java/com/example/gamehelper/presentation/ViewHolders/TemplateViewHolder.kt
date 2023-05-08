package com.example.gamehelper.presentation.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehelper.R

class TemplateViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val name = view.findViewById<TextView>(R.id.tvName)
}