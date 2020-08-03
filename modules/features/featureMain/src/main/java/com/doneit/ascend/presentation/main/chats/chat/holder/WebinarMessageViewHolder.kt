package com.doneit.ascend.presentation.main.chats.chat.holder

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
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
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        binding.apply {
            this.memberEntity = memberEntity
            this.messageEntity = messageEntity
            messageContainer.isRightSwipeEnabled = messageEntity.userId == currentUserId
            if (messageEntity.userId == currentUserId) {
                setOtherLayout()
            } else {
                setOwnLayout()
            }
            ibDelete.setOnClickListener {
                onDeleteClick.invoke(messageEntity)
            }
            userImage.setOnLongClickListener {
                onImageLongClick(it, memberEntity.id)
                true
            }
            userImage.setOnClickListener {
                onImageClick(it, memberEntity)
            }
            sendTime = root.context.getTimeFormat().format(messageEntity.createdAt!!)
            time.text =
                START_TIME_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
            time.visible(
                nextMessage == null || calculateDate(
                    messageEntity.createdAt!!,
                    nextMessage.createdAt!!
                )
            )
            if (nextMessage == null) {
                userImage.visible()
                isOnline.visible(memberEntity.online)
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
            return WebinarMessageViewHolder(
                binding,
                onDeleteClick,
                onMenuClick,
                onImageClick
            )
        }
    }
}