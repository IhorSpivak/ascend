package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.source.storage.local.data.chat.ChatWithLastMessage
import com.doneit.ascend.source.storage.remote.data.response.ChatResponse

fun ChatResponse.toEntity(): ChatEntity {
    return ChatEntity(
        id = id,
        title = title,
        membersCount = membersCount,
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate(),
        online = online,
        blocked = blocked,
        unreadMessageCount = unreadMessageCount,
        chatOwnerId = chatOwnerId,
        image = image?.toEntity(),
        lastMessage = lastMessage?.toEntity(),
        members = listOf(),
        chatType = ChatType.valueOf(chatType.toUpperCase()),
        isPrivate = isPrivate,
        isSubscribed = subscribed,
        owner = owner?.toEntity()
    )
}

fun ChatWithLastMessage.toEntity(): ChatEntity {
    with(chatLocal) {
        return ChatEntity(

            id = id,
            title = title,
            membersCount = membersCount,
            createdAt = createdAt?.toDate(),
            updatedAt = updatedAt?.toDate(),
            online = online,
            blocked = blocked,
            unreadMessageCount = unreadMessageCount,
            chatOwnerId = chatOwnerId,
            image = image?.toEntity(),
            lastMessage = lastMessage?.toEntity(),
            members = members?.map { it.toEntity() },
            chatType = ChatType.valueOf(chatType.toUpperCase()),
            isPrivate = isPrivate,
            isSubscribed = subscribed,
            owner = owner?.toEntity()
        )
    }
}