package com.doneit.ascend.presentation.main.chats.chat.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.invisible
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding
import com.doneit.ascend.presentation.utils.extensions.*

class MessageViewHolder(
    private val binding: ListItemMessageBinding,
    private val onDeleteClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (view: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit
) : BaseMessageHolder(
    binding.root
) {

    override fun bind(
        messageEntity: MessageEntity,
        member: MemberEntity,
        user: UserEntity,
        nextMessage: MessageEntity?,
        chat: ChatEntity
    ) {
        when (messageEntity.type) {
            MessageType.INVITE -> {
                setSystemMessage(
                    binding.root.context.resources.getString(
                        R.string.invite_message,
                        getMemberNameById(chat, chat.chatOwnerId, user, binding.root.context),
                        getMemberNameById(chat, member.id, user, binding.root.context)
                    )
                )
            }
            MessageType.LEAVE -> {
                setSystemMessage(
                    binding.root.context.resources.getString(
                        R.string.leave_message,
                        getMemberNameById(chat, member.id, user, binding.root.context)
                    )
                )
            }
            MessageType.USER_REMOVED -> {
                setSystemMessage(
                    binding.root.context.resources.getString(
                        R.string.remove_message,
                        getMemberNameById(chat, messageEntity.userId, user, binding.root.context)
                    )
                )
            }
            else -> {
                binding.apply {
                    if (messageEntity.userId != user.id) {
                        itemLayout.gone()
                        memberMessageContainer.visible()
                    } else {
                        itemLayout.visible()
                        memberMessageContainer.invisible()
                    }
                    this.memberEntity = member
                    this.messageEntity = messageEntity
                    this.user = user
                    ibDelete.setOnClickListener {
                        onDeleteClick.invoke(messageEntity)
                    }
                    userImage.setOnLongClickListener {
                        if (chat.members?.size != ChatFragment.PRIVATE_CHAT_MEMBER_COUNT) {
                            if (chat.chatOwnerId == user.id) {
                                onImageLongClick(it, member.id)
                                true
                            } else {
                                false
                            }
                        } else {
                            false
                        }
                    }
                    userImage.setOnClickListener {
                        onImageClick(it, member.id)
                    }
                    this.sendTime = root.context.getTimeFormat().format(messageEntity.createdAt!!)
                    if (nextMessage == null) {
                        time.text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                        time.visible()
                        corner.visible()
                        userImage.visible()
                        isOnline.visible(member.online)
                    } else {
                        time.apply {
                            text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                            corner.apply {
                                if (messageEntity.userId == nextMessage.userId) {
                                    userImage.invisible()
                                    isOnline.gone()
                                    this.gone()
                                } else {
                                    userImage.visible()
                                    isOnline.visible(member.online)
                                    this.visible()
                                }
                            }
                            if (calculateDate(messageEntity.createdAt!!, nextMessage.createdAt!!)) {
                                this.visible()
                            } else {
                                this.gone()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setSystemMessage(message: String) {
        binding.apply {
            itemLayout.gone()
            memberMessageContainer.gone()
            time.visible()
            time.text = message
        }
    }

    private fun getMemberNameById(
        item: ChatEntity,
        id: Long,
        user: UserEntity,
        context: Context
    ): String {
        item.members?.firstOrNull {
            it.id == id
        }?.let { member ->
            return if (member.id == user.id) {
                context.getString(R.string.you)
            } else {
                member.fullName
            }
        }
        return ""
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (message: MessageEntity) -> Unit,
            onMenuClick: (view: View, id: Long) -> Unit,
            onImageClick: (view: View, id: Long) -> Unit
        ): MessageViewHolder {
            val binding: ListItemMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_message,
                parent,
                false
            )
            return MessageViewHolder(binding, onDeleteClick, onMenuClick, onImageClick)
        }
    }
}