package com.doneit.ascend.presentation.main.chats.chat.common

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemWebinarMessageBinding
import com.doneit.ascend.presentation.utils.extensions.*

class WebinarMessageViewHolder(
    private val binding: ListItemWebinarMessageBinding,
    private val onDeleteClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (view: View, id: Long) -> Unit,
    private val onImageClick: (view: View, member: MemberEntity) -> Unit
) : BaseMessageHolder(binding.root) {

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
                    this.memberEntity = member
                    this.messageEntity = messageEntity
                    this.user = user
                    messageContainer.isRightSwipeEnabled = messageEntity.userId == user.id
                    if (messageEntity.userId == user.id) {
                        setOtherLayout()
                    } else {
                        setOwnLayout()
                    }
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
                        onImageClick(it, member)
                    }
                    this.sendTime = root.context.getTimeFormat().format(messageEntity.createdAt!!)
                    if (nextMessage == null) {
                        time.text = START_TIME_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                        time.visible()
                        userImage.visible()
                        isOnline.visible(member.online)
                    } else {
                        time.apply {
                            text = START_TIME_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
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

    private fun ListItemWebinarMessageBinding.setOtherLayout() {
        (messageCard.layoutParams as? FrameLayout.LayoutParams)?.gravity =
            Gravity.END
        messageCard.setCardBackgroundColor(
            ContextCompat.getColor(
                messageContainer.context,
                R.color.colorAccent
            )
        )
        userName.setTextColor(Color.WHITE)
        messageTime.setTextColor(Color.WHITE)
        message.setTextColor(Color.WHITE)
        frameContainer.invalidate()
        messageCard.invalidate()
    }

    private fun ListItemWebinarMessageBinding.setOwnLayout() {
        (messageCard.layoutParams as? FrameLayout.LayoutParams)?.gravity =
            Gravity.START
        messageCard.setCardBackgroundColor(Color.WHITE)
        userName.setTextColor(
            ContextCompat.getColor(
                messageCard.context,
                R.color.light_gray_8f
            )
        )
        val textColor = ContextCompat.getColor(
            messageCard.context,
            R.color.colorAccent
        )
        messageTime.setTextColor(textColor)
        message.setTextColor(textColor)
        frameContainer.invalidate()
        messageCard.invalidate()
    }

    private fun setSystemMessage(message: String) {
        binding.apply {
            messageContainer.gone()
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
            onImageClick: (view: View, member: MemberEntity) -> Unit
        ): WebinarMessageViewHolder {
            val binding: ListItemWebinarMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_webinar_message,
                parent,
                false
            )
            return WebinarMessageViewHolder(binding, onDeleteClick, onMenuClick, onImageClick)
        }
    }
}