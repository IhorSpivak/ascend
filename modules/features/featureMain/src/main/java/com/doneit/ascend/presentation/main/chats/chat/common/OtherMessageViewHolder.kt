package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherMessageBinding
import com.doneit.ascend.presentation.utils.extensions.*

class OtherMessageViewHolder(
    itemView: View,
    private val onImageLongClick: (view: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit
) : BaseMessageHolder(itemView) {

    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        DataBindingUtil.bind<ListItemOtherMessageBinding>(itemView)?.apply {
            this.memberEntity = memberEntity
            this.messageEntity = messageEntity
            userImage.setOnLongClickListener {
                onImageLongClick(it, memberEntity.id)
                true
            }
            userImage.setOnClickListener {
                onImageClick(it, memberEntity.id)
            }
            messageTime.text = root.context.getTimeFormat().format(messageEntity.createdAt!!)
            time.apply {
                text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                visibleOrGone(
                    nextMessage == null || calculateDate(
                        messageEntity.createdAt!!,
                        nextMessage.createdAt!!
                    )
                )
                userImage.visible(messageEntity.userId != nextMessage?.userId)
                corner.visible(nextMessage == null || messageEntity.userId != nextMessage.userId)
                isOnline.visible(memberEntity.online && messageEntity.userId != nextMessage?.userId)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onImageLongClick: (view: View, id: Long) -> Unit,
            onImageClick: (view: View, id: Long) -> Unit
        ): OtherMessageViewHolder {
            return OtherMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_other_message, parent, false),
                onImageLongClick,
                onImageClick
            )
        }
    }
}