package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.ProfileEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.ProfileResponse
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
        location,
        createdAt,
        updatedAt,
        meetingStarted,
        newGroups,
        inviteToMeeting,
        unansweredQuestions,
        image?.toEntity(),
        displayName,
        description,
        bio,
        rating,
        role,
        role?.isMasterMind()?:false,
        community
    )
}

fun UserResponse.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id,
        name,
        email,
        phone,
        location,
        createdAt,
        updatedAt,
        meetingStarted,
        newGroups,
        inviteToMeeting,
        unansweredQuestions,
        image?.toEntity(),
        displayName,
        description,
        bio,
        rating,
        role,
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
        location = this@toUserEntity.location,
        createdAt = this@toUserEntity.createdAt,
        updatedAt = this@toUserEntity.updatedAt,
        meetingStarted = this@toUserEntity.meetingStarted,
        newGroups = this@toUserEntity.newGroups,
        inviteToMeeting = this@toUserEntity.inviteToMeeting,
        displayName = this@toUserEntity.displayName,
        description = this@toUserEntity.description,
        bio = this@toUserEntity.bio,
        rating = this@toUserEntity.rating,
        role = this@toUserEntity.role,
        isMasterMind = this@toUserEntity.isMasterMind,
        community = this@toUserEntity.community,
        unansweredQuestions = listOf(),
        image = null
    )
}