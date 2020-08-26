package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.chat.*

fun ChatEntity.toLocal(): ChatWithLastMessage {
    return ChatWithLastMessage(
        ChatLocal(
            id = id,
            membersCount = membersCount,
            createdAt = createdAt?.toRemoteString(),
            updatedAt = lastMessage?.let { it.createdAt.toRemoteString() }
                ?: updatedAt?.toRemoteString(),
            online = online,
            blocked = blocked,
            unreadMessageCount = unreadMessageCount,
            chatOwnerId = chatOwnerId,
            title = title,
            image = image?.toLocal(),
            lastMessageId = lastMessage?.id,
            members = members.map { it.toLocal() },
            chatType = chatType.toString(),
            isPrivate = isPrivate,
            subscribed = isSubscribed,
            owner = owner?.toLocal(),
            description = description.orEmpty()
        ), lastMessage?.toLocal(id)
    )
}

fun MessageEntity.toLocal(chatId: Long): MessageWithPost {
    return MessageWithPost(
        MessageLocal(
            id,
            message,
            userId,
            edited,
            type.toString(),
            createdAt.time,
            updatedAt?.time,
            status.toString(),
            chatId,
            postId = post?.id,
            attachment = attachment?.toLocal(),
            sharedGroup = sharedGroup?.toLocal(),
            sharedUser = sharedUser?.toUserLocal()
        ),
        post = post?.toLocal()
    )
}

fun MessageAttachment.toLocal(): MessageAttachmentLocal {
    return MessageAttachmentLocal(
        name,
        size,
        type.ordinal,
        url
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

fun BlockedUserEntity.toLocal(): BlockedUserLocal {
    return BlockedUserLocal(
        id,
        fullName,
        image?.toLocal()
    )
}