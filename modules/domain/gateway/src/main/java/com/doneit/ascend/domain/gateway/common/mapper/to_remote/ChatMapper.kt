package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.mimeToAttachmentType
import com.doneit.ascend.source.storage.local.data.chat.MessageAttachmentLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageWithPost
import com.doneit.ascend.source.storage.remote.data.request.*

fun ChatListDTO.toRequest(): MyChatsListRequest {
    return MyChatsListRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        title = title,
        createdAtFrom = createdAtFrom,
        createdAtTo = createdAtTo,
        updatedAtTo = updatedAtTo,
        updatedAtFrom = updatedAtFrom,
        chatType = chatType?.toString(),
        allChannels = allChannels
    )
}

fun ChatListDTO.toRequest(page: Int): MyChatsListRequest {
    return MyChatsListRequest(
        page = page,
        perPage = perPage,
        sortColumn = sortColumn,
        sortType = sortType?.toString(),
        title = title,
        createdAtFrom = createdAtFrom,
        createdAtTo = createdAtTo,
        updatedAtTo = updatedAtTo,
        updatedAtFrom = updatedAtFrom,
        chatType = chatType?.toString(),
        allChannels = allChannels,
        ownerId = ownerId,
        community = community
    )
}

fun CreateChatDTO.toRequest(): CreateChatRequest {
    return CreateChatRequest(
        title,
        members
    )
}

fun NewChannelDTO.toRequest(): CreateChannelRequest {
    return CreateChannelRequest(
        title = title,
        image = image,
        description = description,
        isPrivate = isPrivate,
        invites = invites
    )
}

fun MessageListDTO.toRequest(page: Int): MessageListRequest {
    return MessageListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        message,
        status.toString(),
        userId,
        edited?.let {
            if (it) {
                1
            } else {
                0
            }
        },
        createdAtFrom,
        createdAtTo,
        updatedAtTo,
        updatedAtFrom
    )
}

fun MemberListDTO.toRequest(page: Int): MemberListRequest {
    return MemberListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName,
        online?.let {
            if (it) {
                1
            } else {
                0
            }
        }
    )
}

fun MessageDTO.toRequest(): MessageRequest {
    return MessageRequest(
        id,
        message,
        if (attachmentUrl.isEmpty()) {
            null
        } else {
            AttachmentRequest(
                contentType = attachmentType,
                url = attachmentUrl
            )
        }
    )
}

fun MessageDTO.toMessageWithPost(filename: String): MessageWithPost {
    return MessageWithPost(
        MessageLocal(
            -1L,
            message = message,
            userId = userId,
            edited = false,
            type = MessageType.ATTACHMENT.toString(),
            createdAt = System.currentTimeMillis(),
            updatedAt = null,
            status = "sent",
            chatId = id,
            postId = null,
            attachment = MessageAttachmentLocal(
                filename,
                "",
                attachmentType.mimeToAttachmentType().ordinal,
                attachmentUrl
            ),
            sharedGroup = null,
            sharedUser = null
        ), null
    )
}

fun BlockedUsersDTO.toRequest(page: Int): BlockedUsersRequest {
    return BlockedUsersRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName
    )
}