package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.remote.data.response.ChatResponse

fun ChatResponse.toEntity(): ChatEntity {
    return ChatEntity(
        id,
        title,
        membersCount,
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate(),
        online = online,
        unreadMessageCount = unreadMessageCount,
        chatOwnerId = chatOwnerId,
        image = image?.toEntity(),
        lastMessage = lastMessage?.toEntity(),
        members = listOf()
    )
}

fun ChatLocal.toEntity(): ChatEntity {
    return ChatEntity(
        id,
        title,
        membersCount,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        online,
        unreadMessageCount,
        chatOwnerId,
        image?.toEntity(),
        lastMessage?.toEntity(),
        members?.map { it.toEntity() }
    )
}