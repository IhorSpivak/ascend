package com.doneit.ascend.presentation.video_chat.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.UserEntity

class UserDiffCallback(
    private val oldItems: List<UserEntity>,
    private val newItems: List<UserEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size
}