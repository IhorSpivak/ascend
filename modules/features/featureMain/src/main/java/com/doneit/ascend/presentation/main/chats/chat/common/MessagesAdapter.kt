package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity

class MessagesAdapter(
    val type: ChatType,
    var chat: ChatEntity?,
    var user: UserEntity?,
    private val onButtonClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (v: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit
) : PagedListAdapter<MessageEntity, BaseMessageHolder>(MessageDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageHolder {
        return when (viewType) {
            ChatType.CHAT.ordinal -> MessageViewHolder.create(
                parent,
                onButtonClick,
                onImageLongClick,
                onImageClick
            )
            ChatType.WEBINAR_CHAT.ordinal -> WebinarMessageViewHolder.create(
                parent,
                onButtonClick,
                onImageLongClick,
                onImageClick
            )
            else -> throw IllegalArgumentException("Unknown view type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseMessageHolder, position: Int) {
        getItem(position)?.let { message ->
            chat?.members?.firstOrNull {
                it.id == message.userId
            }?.let { member ->
                user?.let { user ->
                    if (position != (itemCount - 1)) {
                        holder.bind(
                            message,
                            member,
                            user,
                            getItem(position + 1),
                            chat!!

                        )
                    } else {
                        holder.bind(message, member, user, null, chat!!)
                    }
                }

            }
        }
    }

    fun updateMembers(chat: ChatEntity) {
        if (this.chat != chat) {
            this.chat = chat
            notifyItemRangeChanged(0, itemCount - 1)
        }
    }

    fun updateUser(user: UserEntity) {
        if (this.user != user) {
            this.user = user
            notifyItemRangeChanged(0, itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type.ordinal
    }
}