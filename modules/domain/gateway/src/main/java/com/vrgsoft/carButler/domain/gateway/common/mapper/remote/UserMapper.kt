package com.vrgsoft.carButler.domain.gateway.common.mapper.remote

import com.vrgsoft.carButler.domain.entity.User
import com.vrgsoft.carButler.source.storage.remote.data.response.LoginResponse

fun LoginResponse.toEntity(): User {
    return User(
        user.name,
        user.email,
        user.phone,
        user.createdAt,
        user.updatedAt
    )
}