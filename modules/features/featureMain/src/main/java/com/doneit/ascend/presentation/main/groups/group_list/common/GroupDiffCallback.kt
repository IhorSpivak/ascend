package com.doneit.ascend.presentation.main.groups.group_list.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.group.GroupEntity

class GroupDiffCallback : DiffUtil.ItemCallback<GroupEntity>() {
    override fun areItemsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
        return oldItem == newItem
    }
}