package com.doneit.ascend.presentation.profile.mm_following.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.MasterMindEntity


class FollowingDiffCallback : DiffUtil.ItemCallback<MasterMindEntity>() {
    override fun areItemsTheSame(oldItem: MasterMindEntity, newItem: MasterMindEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MasterMindEntity, newItem: MasterMindEntity): Boolean {
        return oldItem == newItem
    }
}