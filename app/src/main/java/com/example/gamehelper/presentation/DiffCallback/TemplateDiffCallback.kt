package com.example.gamehelper.presentation.DiffCallback

import androidx.recyclerview.widget.DiffUtil
import com.example.gamehelper.domain.entity.Template

class TemplateDiffCallback: DiffUtil.ItemCallback<Template>() {
    override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
        return oldItem == newItem
    }
}