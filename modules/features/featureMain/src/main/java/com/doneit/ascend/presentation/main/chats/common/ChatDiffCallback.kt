package com.doneit.ascend.presentation.main.chats.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.chats.ChatEntity

class ChatDiffCallback : DiffUtil.ItemCallback<ChatEntity>() {
    override fun areItemsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatEntity, newItem: ChatEntity): Boolean {
        return oldItem == newItem
    }
}