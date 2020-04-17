package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.source.storage.remote.data.request.CreateChatRequest
import com.doneit.ascend.source.storage.remote.data.request.MyChatsListRequest

fun ChatListDTO.toRequest(): MyChatsListRequest {
    return MyChatsListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        title,
        createdAtFrom,
        createdAtTo,
        updatedAtTo,
        updatedAtFrom
    )
}

fun ChatListDTO.toRequest(page: Int): MyChatsListRequest {
    return MyChatsListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        title,
        createdAtFrom,
        createdAtTo,
        updatedAtTo,
        updatedAtFrom
    )
}

fun CreateChatDTO.toRequest(): CreateChatRequest {
    return CreateChatRequest(
        title,
        members
    )
}