package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.UserResponse

fun AuthResponse.toEntity(): AuthEntity {
    return AuthEntity(
        token,
        user.toEntity()
    )
}

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        name,
        email,
        phone,
        createdAt,
        updatedAt,
        unansweredQuestions,
        rating ?: 0,
        role,
        community
    )
}