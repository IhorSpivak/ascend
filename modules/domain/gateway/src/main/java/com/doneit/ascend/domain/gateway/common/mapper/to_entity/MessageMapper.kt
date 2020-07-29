package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toEntity
import com.doneit.ascend.source.storage.local.data.chat.MessageAttachmentLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageWithPost
import com.doneit.ascend.source.storage.remote.data.response.MessageAttachmentResponse
import com.doneit.ascend.source.storage.remote.data.response.MessageResponse
import java.util.*

fun MessageResponse.toEntity(): MessageEntity {
    return MessageEntity(
        id,
        message.orEmpty(),
        edited?.let { edited } ?: false,
        messageType?.toMessageType() ?: MessageType.MESSAGE,
        userId,
        createdAt.toDate(),
        updatedAt.toDate(),
        status = status.toMessageStatus(),
        post = post?.toEntity(),
        attachment = attachment?.toEntity()
    )
}

fun MessageAttachmentResponse.toEntity(): MessageAttachment {
    return MessageAttachment(
        name = name.orEmpty(),
        size = size.orEmpty(),
        type = type.orEmpty().toAttachmentType(),
        url = url.orEmpty()
    )
}

fun MessageWithPost.toEntity(): MessageEntity {
    return MessageEntity(
        messageLocal.id,
        messageLocal.message,
        messageLocal.edited,
        messageLocal.type.toMessageType(),
        messageLocal.userId,
        messageLocal.createdAt?.let { Date(it) },
        messageLocal.updatedAt?.let { Date(it) },
        status = messageLocal.status.toMessageStatus(),
        post = post?.toEntity(),
        attachment = messageLocal.attachment?.toEntity()
    )
}

fun MessageAttachmentLocal.toEntity(): MessageAttachment {
    return MessageAttachment(
        name,
        size,
        AttachmentType.values()[type],
        url
    )
}

fun String.toMessageType(): MessageType {
    return when (this) {
        MessageType.POST_SHARE.toString() -> MessageType.POST_SHARE
        MessageType.INVITE.toString() -> MessageType.INVITE
        MessageType.LEAVE.toString() -> MessageType.LEAVE
        MessageType.ATTACHMENT.toString() -> MessageType.ATTACHMENT
        MessageType.USER_REMOVED.toString() -> MessageType.USER_REMOVED
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