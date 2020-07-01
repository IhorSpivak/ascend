package com.doneit.ascend.presentation.main.chats.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity

class ChatMembersDiffUtilCallback : DiffUtil.ItemCallback<MemberEntity>() {
    override fun areItemsTheSame(oldItem: MemberEntity, newItem: MemberEntity): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: MemberEntity, newItem: MemberEntity): Boolean {
        return newItem.equals(oldItem)
    }
}