package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toEntity
import com.doneit.ascend.source.storage.local.data.chat.MessageWithPost
import com.doneit.ascend.source.storage.remote.data.response.MessageResponse
import java.util.*

fun MessageResponse.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message.orEmpty(),
        edited?.let { edited } ?: false,
        messageType?.toMessageType()?: MessageType.MESSAGE,
        userId,
        createdAt.toDate(),
        updatedAt.toDate(),
        status = status.toMessageStatus(),
        post = post?.toEntity()
    )
}

fun MessageWithPost.toEntity(): MessageEntity {
    return MessageEntity(
        messageLocal.id,
        messageLocal.message,
        messageLocal.edited,
        messageLocal.type.toMessageType(),
        messageLocal.userId,
        messageLocal.createdAt?.let{ Date(it) },
        messageLocal.updatedAt?.let{Date(it)},
        status = messageLocal.status.toMessageStatus(),
        post = post?.toEntity()
    )
}
fun String.toMessageType(): MessageType {
    return when{
        this == MessageType.POST_SHARE.toString() -> MessageType.POST_SHARE
        this == MessageType.INVITE.toString() -> MessageType.INVITE
        this == MessageType.LEAVE.toString() -> MessageType.LEAVE
        this == MessageType.USER_REMOVED.toString() -> MessageType.USER_REMOVED
        else -> MessageType.MESSAGE
    }
}
fun String?.toMessageStatus(): MessageStatus {
    return when (this) {
        "sent" -> MessageStatus.SENT
        "delivered" -> MessageStatus.DELIVERED
        "read" -> MessageStatus.READ
        else -> MessageStatus.ALL
    }
}