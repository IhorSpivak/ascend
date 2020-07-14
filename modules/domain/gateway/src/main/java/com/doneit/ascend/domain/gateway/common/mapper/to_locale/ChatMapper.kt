package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

fun ChatEntity.toLocal(): ChatLocal {
    return ChatLocal(
        id = id,
        membersCount = membersCount,
        createdAt = createdAt?.toRemoteString(),
        updatedAt = lastMessage?.let { it.createdAt?.toRemoteString() } ?: updatedAt?.toRemoteString(),
        online = online,
        blocked = blocked,
        unreadMessageCount = unreadMessageCount,
        chatOwnerId = chatOwnerId,
        title = title,
        image = image?.toLocal(),
        lastMessage = lastMessage?.toLocal(id),
        members = members?.map { it.toLocal() },
        chatType = chatType.toString(),
        isPrivate = isPrivate,
        subscribed = isSubscribed
    )
}

fun MessageEntity.toLocal(chatId: Long): MessageLocal {
    return MessageLocal(
        id,
        message,
        userId,
        edited,
        type.toString(),
        createdAt?.time,
        updatedAt?.time,
        status.toString(),
        chatId
    )
}

fun MemberEntity.toLocal(): MemberLocal {
    return MemberLocal(
        id,
        fullName,
        online,
        leaved,
        removed,
        image?.toLocal()
    )
}

fun BlockedUserEntity.toLocal(): BlockedUserLocal{
    return BlockedUserLocal(
        id,
        fullName,
        image?.toLocal()
    )
}