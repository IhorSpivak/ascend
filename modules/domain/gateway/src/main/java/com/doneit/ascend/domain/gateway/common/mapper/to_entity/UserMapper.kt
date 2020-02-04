package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.RateResponse
import com.doneit.ascend.source.storage.remote.data.response.UserResponse

fun AuthResponse.toEntity(): AuthEntity {
    return AuthEntity(
        token,
        user.toEntity()
    )
}

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        id,
        name,
        email,
        phone,
        location,
        createdAt?.toDate(),
        updatedAt?.toDate(),
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
        community,
        visitedGroupsCount,
        birthday?.toShortDate()
    )
}

fun UserLocal.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        fullName = this@toUserEntity.name,
        email = this@toUserEntity.email,
        phone = this@toUserEntity.phone,
        location = this@toUserEntity.location,
        createdAt = this@toUserEntity.createdAt?.toDate(),
        updatedAt = this@toUserEntity.updatedAt?.toDate(),
        meetingStarted = this@toUserEntity.meetingStarted,
        newGroups = this@toUserEntity.newGroups,
        inviteToMeeting = this@toUserEntity.inviteToMeeting,
        displayName = this@toUserEntity.displayName,
        description = this@toUserEntity.description,
        bio = this@toUserEntity.bio,
        rating = this@toUserEntity.rating,
        role = this@toUserEntity.role,
        community = this@toUserEntity.community,
        unansweredQuestions = listOf(),
        image = ImageEntity(
            if(imageURL?.isBlank() == true) null else imageURL,
            ThumbnailEntity(
                if(thumbURL?.isBlank() == true) null else thumbURL
            )
        ),
        visitedGroupCount = this@toUserEntity.visitedGroupCount,
        birthday = this@toUserEntity.birthday?.toShortDate()
    )
}

fun RateResponse.toEntity(): RateEntity {
    return RateEntity(
        value,
        userId,
        fullName,
        image.toEntity(),
        createdAt.toDate()!!,
        updatedAt.toDate()
    )
}