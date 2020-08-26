package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.databinding.ListItemOtherMessageBinding
import com.doneit.ascend.presentation.utils.extensions.*

class OtherMessageViewHolder(
    itemView: View,
    private val onImageLongClick: (view: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit,
    private val onImageWebinarClick: (view: View, member: MemberEntity) -> Unit,
    private val chatType: ChatType
) : BaseMessageHolder(itemView) {

    fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity
    ) {
        DataBindingUtil.bind<ListItemOtherMessageBinding>(itemView)?.apply {
            this.memberEntity = memberEntity
            this.messageEntity = messageEntity
            userImage.setOnLongClickListener {
                onImageLongClick(it, memberEntity.id)
                true
            }
            userImage.setOnClickListener {
                if(chatType == ChatType.CHAT) {
                    onImageClick(it, memberEntity.id)
                } else {
                    onImageWebinarClick(it, memberEntity)
                }
            }
            messageTime.text = root.context.getTimeFormat().format(messageEntity.createdAt)
            time.apply {
                text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt)
                visibleOrGone(
                    nextMessage == null || calculateDate(
                        messageEntity.createdAt,
                        nextMessage.createdAt
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
            onImageClick: (view: View, id: Long) -> Unit,
            onImageWebinarClick: (view: View, member: MemberEntity) -> Unit,
            chatType: ChatType
        ): OtherMessageViewHolder {
            return OtherMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_other_message, parent, false),
                onImageLongClick,
                onImageClick,
                onImageWebinarClick,
                chatType
            )
        }
    }
}