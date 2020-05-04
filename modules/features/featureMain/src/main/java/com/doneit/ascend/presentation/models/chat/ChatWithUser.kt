package com.doneit.ascend.presentation.models.chat

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity

data class ChatWithUser(
    val chat: ChatEntity,
    val user: UserEntity
)

data class ChatsWithUser(
    val chat: PagedList<ChatEntity>,
    val user: UserEntity
)