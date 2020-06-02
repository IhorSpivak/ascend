package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity

class MessagesAdapter(
    var chat: ChatEntity?,
    var user: UserEntity?,
    private val onButtonClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (v: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit
) : PagedListAdapter<MessageEntity, MessageViewHolder>(MessageDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.create(parent, onButtonClick, onImageLongClick, onImageClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.let {message ->
            chat?.members?.firstOrNull {
                it.id == message.userId
            }?.let {member ->
                user?.let {user ->
                    if (position != (itemCount -1)) {
                        holder.bind(
                            message,
                            member,
                            user,
                            getItem(position + 1),
                            chat!!

                        )
                    }else{
                        holder.bind(message, member, user, null, chat!!)
                    }
                }

            }
        }
    }

    fun updateMembers(chat: ChatEntity) {
        this.chat = chat
        notifyDataSetChanged()
    }
    fun updateUser(user: UserEntity){
        this.user = user
        notifyDataSetChanged()
    }
}