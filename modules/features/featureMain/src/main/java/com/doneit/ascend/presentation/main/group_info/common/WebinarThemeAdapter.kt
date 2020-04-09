package com.doneit.ascend.presentation.main.group_info.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.group.GroupEntity

class WebinarThemeAdapter(
    val groupEntity: GroupEntity
): ListAdapter<String, WebinarThemeViewHolder>(DiffStrings()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebinarThemeViewHolder {
        return WebinarThemeViewHolder.create(parent, groupEntity)
    }

    override fun onBindViewHolder(holder: WebinarThemeViewHolder, position: Int) {
        holder.bind(position)
    }

    private class DiffStrings: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return  newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return  newItem == oldItem
        }

    }
}