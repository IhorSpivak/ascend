package com.doneit.ascend.presentation.main.chats.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity

class BlockedUserDiffCallback: DiffUtil.ItemCallback<BlockedUserEntity>(){
    override fun areItemsTheSame(oldItem: BlockedUserEntity, newItem: BlockedUserEntity): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: BlockedUserEntity, newItem: BlockedUserEntity): Boolean {
        return newItem == oldItem
    }
}