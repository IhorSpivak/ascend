package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment

class MessagesAdapter(
    val type: ChatType,
    var chat: ChatEntity?,
    var user: UserEntity?,
    private val onButtonClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (v: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit,
    private val onImageWebinarClick: (view: View, member: MemberEntity) -> Unit
) : PagedListAdapter<MessageEntity, BaseMessageHolder>(MessageDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageHolder {
        return when (viewType) {
            Type.OWN.ordinal -> OwnMessageViewHolder.create(
                parent,
                onButtonClick
            )
            Type.SYSTEM.ordinal -> SystemMessageViewHolder.create(
                parent
            )
            Type.OTHER.ordinal -> OtherMessageViewHolder.create(
                parent,
                { view: View, id: Long ->
                    if (chat?.membersCount == ChatFragment.PRIVATE_CHAT_MEMBER_COUNT && chat?.chatOwnerId == user?.id)
                        onImageLongClick.invoke(view, id)
                },
                onImageClick
            )
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseMessageHolder, position: Int) {
        val message = getItem(position) ?: return
        val nextMessage = if (position != itemCount - 1) getItem(position + 1) else null
        val chat = chat ?: return
        val member = chat.members?.firstOrNull { it.id == message.userId } ?: return
        val user = user ?: return
        holder.bind(
            messageEntity = message,
            nextMessage = nextMessage,
            memberEntity = member,
            currentUserId = user.id,
            chatOwner = chat.members?.firstOrNull { it.id == chat.chatOwnerId } ?: return
        )
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
        return convertItemToType(getItem(position)!!).ordinal
    }

    private fun convertItemToType(message: MessageEntity): Type {
        return when (message.type) {
            MessageType.MESSAGE -> if (message.userId == user?.id) {
                Type.OWN
            } else Type.OTHER
            MessageType.INVITE,
            MessageType.USER_REMOVED,
            MessageType.LEAVE -> Type.SYSTEM
        }
    }

    enum class Type {
        OWN,
        SYSTEM,
        OTHER
    }
}