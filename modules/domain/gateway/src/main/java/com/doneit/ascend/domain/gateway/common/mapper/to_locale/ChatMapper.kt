package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

fun ChatEntity.toLocal(): ChatLocal {
    return ChatLocal(
        id,
        membersCount,
        createdAt?.toRemoteString(),
        updatedAt?.toRemoteString(),
        online,
        unreadMessageCount,
        chatOwnerId,
        title,
        image?.toLocal(),
        lastMessage?.toLocal()
    )
}

fun MessageEntity.toLocal(): MessageLocal {
    return MessageLocal(
        id,
        message,
        userId,
        edited,
        createdAt?.toRemoteString(),
        updatedAt?.toRemoteString(),
        status.toString()
    )
}