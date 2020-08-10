package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.chats.chat.holder.*
import com.doneit.ascend.presentation.models.chat.ChatWithUser

class MessagesAdapter(
    private val chatWithUser: ChatWithUser,
    private val onButtonClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (v: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit,
    private val onImageWebinarClick: (view: View, member: MemberEntity) -> Unit
) : PagedListAdapter<MessageEntity, BaseMessageHolder>(MessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageHolder {
        return when (viewType) {
            Type.OWN.ordinal -> if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) OwnMessageViewHolder.create(
                parent,
                onButtonClick
            ) else WebinarMessageViewHolder.create(parent, onButtonClick, { view: View, l: Long -> }, onImageWebinarClick)
            Type.SYSTEM.ordinal -> SystemMessageViewHolder.create(
                parent
            )
            Type.OTHER.ordinal -> if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) OtherMessageViewHolder.create(
                parent,
                { view: View, id: Long ->
                    with(chatWithUser) {
                        if (chat.membersCount == ChatFragment.PRIVATE_CHAT_MEMBER_COUNT && chat.chatOwnerId == user.id) {
                            onImageLongClick.invoke(view, id)
                        }
                    }
                },
                onImageClick,
                onImageWebinarClick,
                chatWithUser.chatType
            ) else WebinarMessageViewHolder.create(parent, onButtonClick, { view: View, l: Long -> }, onImageWebinarClick)
            Type.SHARE.ordinal -> ShareViewHolder.create(parent)
            Type.ATTACHMENT_OWN.ordinal -> AttachmentOwnViewHolder.create(
                parent,
                onButtonClick
            )
            Type.ATTACHMENT_OTHER.ordinal -> AttachmentOtherViewHolder.create(
                parent
            )
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseMessageHolder, position: Int) {
        val message = getItem(position)!!
        val nextMessage = if (position != itemCount - 1) getItem(position + 1) else null
        val member = chatWithUser.chat.members.firstOrNull { it.id == message.userId } ?: return
        holder.bind(
            messageEntity = message,
            nextMessage = nextMessage,
            memberEntity = member,
            currentUserId = chatWithUser.user.id,
            chatOwner = chatWithUser.chat.members.firstOrNull {
                it.id == chatWithUser.chat.chatOwnerId
            } ?: return
        )
    }

    override fun getItemViewType(position: Int): Int {
        return convertItemToType(getItem(position)!!).ordinal
    }

    private fun convertItemToType(message: MessageEntity): Type {
        return when (message.type) {
            MessageType.POST_SHARE -> Type.SHARE
            MessageType.ATTACHMENT -> if (message.userId == chatWithUser.user.id) {
                Type.ATTACHMENT_OWN
            } else Type.ATTACHMENT_OTHER
            MessageType.MESSAGE -> if (message.userId == chatWithUser.user.id) {
                Type.OWN
            } else Type.OTHER
            MessageType.INVITE,
            MessageType.USER_REMOVED,
            MessageType.LEAVE -> Type.SYSTEM
            else -> Type.OTHER
        }
    }

    enum class Type {
        OWN,
        SYSTEM,
        OTHER,
        SHARE,
        ATTACHMENT_OWN,
        ATTACHMENT_OTHER
    }
}