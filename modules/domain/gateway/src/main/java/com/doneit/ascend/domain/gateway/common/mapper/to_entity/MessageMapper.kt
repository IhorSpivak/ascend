package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal
import com.doneit.ascend.source.storage.remote.data.response.MessageResponse
import java.util.*

fun MessageResponse.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message,
        edited?.let { edited } ?: false,
        userId,
        createdAt.toDate(),
        updatedAt.toDate(),
        status = status.toMessageStatus()
    )
}

fun MessageLocal.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message,
        edited,
        userId,
        createdAt?.let{ Date(it) },
        updatedAt?.let{Date(it)},
        status = status.toMessageStatus()
    )
}

fun String.toMessageStatus(): MessageStatus {
    return when (this) {
        "sent" -> MessageStatus.SENT
        "delivered" -> MessageStatus.DELIVERED
        "read" -> MessageStatus.READ
        else -> MessageStatus.ALL
    }
}