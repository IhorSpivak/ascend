package com.doneit.ascend.presentation.main.master_mind.list.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.MasterMindEntity

class MasterMindDiffCallback : DiffUtil.ItemCallback<MasterMindEntity>() {
    override fun areItemsTheSame(oldItem: MasterMindEntity, newItem: MasterMindEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MasterMindEntity, newItem: MasterMindEntity): Boolean {
        return oldItem.equals(newItem)
    }
}