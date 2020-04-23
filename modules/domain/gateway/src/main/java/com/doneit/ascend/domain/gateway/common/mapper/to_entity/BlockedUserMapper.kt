package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal
import com.doneit.ascend.source.storage.remote.data.response.BlockedUserResponse

fun BlockedUserResponse.toEntity(): BlockedUserEntity {
    return BlockedUserEntity(
        id,
        fullName,
        image?.toEntity()
    )
}

fun BlockedUserLocal.toEntity(): BlockedUserEntity{
    return BlockedUserEntity(
        id,
        fullName,
        image?.toEntity()
    )
}