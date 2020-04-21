package com.doneit.ascend.presentation.main.chats.chat.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.chats.MessageEntity

class MessageDiffUtil: DiffUtil.ItemCallback<MessageEntity>() {
    override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
        return newItem == oldItem
    }
}