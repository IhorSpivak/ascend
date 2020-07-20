package com.doneit.ascend.presentation.main.home.community_feed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.chats.ChatEntity

class ChannelDiffUtilCallback : DiffUtil.ItemCallback<ChatEntity>() {
    override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem == newItem
    }

}