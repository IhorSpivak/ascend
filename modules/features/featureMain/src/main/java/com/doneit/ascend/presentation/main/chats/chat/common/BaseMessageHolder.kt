package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity

abstract class BaseMessageHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(
        messageEntity: MessageEntity,
        member: MemberEntity,
        user: UserEntity,
        nextMessage: MessageEntity?,
        chat: ChatEntity
    )
}