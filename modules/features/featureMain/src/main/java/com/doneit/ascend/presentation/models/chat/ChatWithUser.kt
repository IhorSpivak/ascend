package com.doneit.ascend.presentation.models.chat

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType

data class ChatWithUser(
    val chat: ChatEntity,
    val user: UserEntity,
    val chatType: ChatType
)

data class ChatsWithUser(
    val chat: PagedList<ChatEntity>,
    val user: UserEntity
)

data class ChannelsWithUser(
    val chat: com.doneit.ascend.domain.use_case.PagedList<ChatEntity>,
    val user: UserEntity
)