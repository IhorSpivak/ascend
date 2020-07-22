package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.source.storage.remote.data.request.*
import java.io.File

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
        allChannels = allChannels
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
        image = image?.let { File(it) },
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
        message
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