package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity

class MessagesAdapter(
    var pagedList: PagedList<MemberEntity>?,
    var user: UserEntity?
) : PagedListAdapter<MessageEntity, MessageViewHolder>(MessageDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.let {message ->
            pagedList?.firstOrNull{
                it.id == message.userId
            }?.let {member ->
                user?.let {user ->
                    if (position != (itemCount -1)) {
                        holder.bind(message, member, user, getItem(position + 1))
                    }else{
                        holder.bind(message, member, user, null)
                    }
                }

            }
        }
    }
    fun updateMembers(members: PagedList<MemberEntity>){
        this.pagedList = members
        notifyDataSetChanged()
    }
    fun updateUser(user: UserEntity){
        this.user = user
        notifyDataSetChanged()
    }
}