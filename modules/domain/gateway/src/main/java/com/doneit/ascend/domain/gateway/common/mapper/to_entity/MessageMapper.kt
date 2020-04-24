package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal
import com.doneit.ascend.source.storage.remote.data.response.MessageResponse
import java.util.*

fun MessageResponse.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message?:"",
        edited?.let { edited } ?: false,
        messageType?.toMessageType()?: MessageType.MESSAGE,
        userId,
        createdAt.toDate(),
        updatedAt.toDate(),
        status = status?.toMessageStatus()?: MessageStatus.ALL
    )
}

fun MessageLocal.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message,
        edited,
        type.toMessageType(),
        userId,
        createdAt?.let{ Date(it) },
        updatedAt?.let{Date(it)},
        status = status.toMessageStatus()
    )
}
fun String.toMessageType(): MessageType {
    return when{
        this == MessageType.INVITE.toString() -> MessageType.INVITE
        this == MessageType.LEAVE.toString() -> MessageType.LEAVE
        else -> MessageType.MESSAGE
    }
}
fun String.toMessageStatus(): MessageStatus {
    return when (this) {
        "sent" -> MessageStatus.SENT
        "delivered" -> MessageStatus.DELIVERED
        "read" -> MessageStatus.READ
        else -> MessageStatus.ALL
    }
}