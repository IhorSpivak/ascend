package com.doneit.ascend.domain.gateway.common.mapper.remote

import com.doneit.ascend.source.storage.remote.data.response.LoginResponse

fun LoginResponse.toEntity(): com.doneit.ascend.domain.entity.User {
    return com.doneit.carButler.domain.entity.User(
        user.name,
        user.email,
        user.phone,
        user.createdAt,
        user.updatedAt
    )
}