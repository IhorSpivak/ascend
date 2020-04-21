package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity

class MessagesAdapter : PagedListAdapter<MessageEntity, MessageViewHolder>(MessageDiffUtil()) {
    private var members: List<MemberEntity> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.let {message ->
            /*members.firstOrNull{it.id == message.userId}?.let {
                holder.bind(message, it)
            }*/
            holder.bind(message)
        }
    }
    fun updateMembers(members: List<MemberEntity>){
        this.members = members
    }
}