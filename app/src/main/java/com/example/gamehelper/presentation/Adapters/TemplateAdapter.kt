package com.example.gamehelper.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.gamehelper.R
import com.example.gamehelper.domain.entity.Template
import com.example.gamehelper.presentation.DiffCallback.TemplateDiffCallback
import com.example.gamehelper.presentation.ViewHolders.TemplateViewHolder

class TemplateAdapter: ListAdapter<Template, TemplateViewHolder>(TemplateDiffCallback()) {

    var onTemplateClickListener: ((Template) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val layout = R.layout.template_item
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TemplateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        val template = getItem(position)

        holder.view.setOnClickListener {
            onTemplateClickListener?.invoke(template)
        }

        holder.name.text = template.name

    }

    companion object{
        const val MAX_POOL_SIZE = 30
    }
}
