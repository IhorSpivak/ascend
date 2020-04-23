package com.doneit.ascend.presentation.main.chats.new_chat.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.chats.common.AttendeeDiffCallback

class ChatMemberAdapter(
    private val onDeleteMember: (AttendeeEntity) -> Unit
): ListAdapter<AttendeeEntity, ChatMemberViewHolder>(AttendeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMemberViewHolder {
        return ChatMemberViewHolder.create(parent, onDeleteMember)
    }

    override fun onBindViewHolder(holder: ChatMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}