package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.remote.data.response.MemberResponse

fun MemberResponse.toEntity(): MemberEntity{
    return MemberEntity(
        id,
        fullName,
        online,
        leaved,
        image?.toEntity()
    )
}

fun MemberLocal.toEntity(): MemberEntity{
    return MemberEntity(
        id,
        fullName,
        online,
        leaved,
        image?.toEntity()
    )
}