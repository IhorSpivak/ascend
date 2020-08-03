package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemSystemMessageBinding

class SystemMessageViewHolder(
    itemView: View
) : BaseMessageHolder(itemView) {
    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        DataBindingUtil.bind<ListItemSystemMessageBinding>(itemView)?.apply {
            val ownerName = if(chatOwner.id == currentUserId)
                root.context.getString(R.string.you)
            else chatOwner.fullName
            val memberName = if(memberEntity.id == currentUserId)
                root.context.getString(R.string.you)
            else memberEntity.fullName
            message = when (messageEntity.type) {
                MessageType.INVITE -> {
                    root.context.getString(
                        R.string.invite_message,
                        ownerName,
                        memberName
                    )
                }
                MessageType.LEAVE -> {
                    root.context.getString(
                        R.string.leave_message,
                        memberName
                    )
                }
                MessageType.USER_REMOVED -> {
                    root.context.getString(
                        R.string.remove_message,
                        memberName
                    )
                }
                else -> ""
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SystemMessageViewHolder {
            return SystemMessageViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_system_message, parent, false)
            )
        }
    }
}