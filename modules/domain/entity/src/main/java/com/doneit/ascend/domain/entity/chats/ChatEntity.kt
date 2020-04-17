package com.doneit.ascend.domain.entity.chats

import com.doneit.ascend.domain.entity.ImageEntity
import java.util.*

data class ChatEntity(
    val id: Long,
    val title: String,
    val membersCount: Int,
    val createdAt: Date?,
    val updatedAt: Date?,
    val online: Boolean,
    val unreadMessageCount: Int,
    val chatOwnerId: Long,
    val image: ImageEntity?,
    val lastMessage: MessageEntity?
)