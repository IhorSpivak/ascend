package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity

abstract class BaseMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(
        messageEntity: MessageEntity,
        nextMessage:MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    )
}