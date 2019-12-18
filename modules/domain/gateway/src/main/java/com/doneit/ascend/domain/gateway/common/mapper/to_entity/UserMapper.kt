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

private fun String.isMasterMind(): Boolean {
    return this == "master_mind"
}

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        id,
        name,
        email,
        phone,
        createdAt,
        updatedAt,
        unansweredQuestions,
        rating ?: 0,
        role?.isMasterMind()?:false,
        community
    )
}

fun UserLocal.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = this@toUserEntity.name,
        email = this@toUserEntity.email,
        phone = this@toUserEntity.phone,
        createdAt = this@toUserEntity.createdAt,
        updatedAt = this@toUserEntity.updatedAt,
        isMasterMind = this@toUserEntity.isMasterMind,
        rating = this@toUserEntity.rating,
        community = this@toUserEntity.community,
        unansweredQuestions = listOf()
    )
}