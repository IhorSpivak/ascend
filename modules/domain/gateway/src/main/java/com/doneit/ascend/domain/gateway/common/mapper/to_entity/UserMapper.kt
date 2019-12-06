package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.source.storage.local.data.UserLocal
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

fun UserEntity.toUserLocal(): UserLocal {
    return UserLocal(
        name = this@toUserLocal.name,
        email = this@toUserLocal.email,
        phone = this@toUserLocal.phone,
        createdAt = this@toUserLocal.createdAt,
        updatedAt = this@toUserLocal.updatedAt,
        role = this@toUserLocal.role,
        rating = this@toUserLocal.rating,
        community = this@toUserLocal.community
    )
}

fun UserLocal.toUserEntity(): UserEntity {
    return UserEntity(
        name = this@toUserEntity.name,
        email = this@toUserEntity.email,
        phone = this@toUserEntity.phone,
        createdAt = this@toUserEntity.createdAt,
        updatedAt = this@toUserEntity.updatedAt,
        role = this@toUserEntity.role,
        rating = this@toUserEntity.rating,
        community = this@toUserEntity.community,
        unansweredQuestions = listOf()
    )
}