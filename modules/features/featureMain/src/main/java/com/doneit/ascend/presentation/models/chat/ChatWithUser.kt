package com.doneit.ascend.presentation.models.chat

import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity

data class ChatWithUser(
    val chat: ChatEntity,
    val user: UserEntity
)