package com.doneit.ascend.presentation.main.search.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity

class SearchDiffCallback : DiffUtil.ItemCallback<SearchEntity>() {
    override fun areItemsTheSame(oldItem: SearchEntity, newItem: SearchEntity): Boolean {
        return (oldItem is GroupEntity && newItem is GroupEntity && oldItem.id == newItem.id)
                || (oldItem is MasterMindEntity && newItem is MasterMindEntity && oldItem.id == newItem.id)

    }

    override fun areContentsTheSame(oldItem: SearchEntity, newItem: SearchEntity): Boolean {
        return oldItem == newItem
    }
}