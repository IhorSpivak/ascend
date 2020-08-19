package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.user.AuthEntity
import com.doneit.ascend.domain.entity.user.RegistrationType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.gateway.common.mapper.Constants.MM_ROLE
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.remote.data.response.RateResponse
import com.doneit.ascend.source.storage.remote.data.response.user.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.user.SearchUsersResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserAuthResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserProfileResponse
import com.vrgsoft.core.gateway.orZero

fun AuthResponse.toEntity(): AuthEntity {
    return AuthEntity(
        token,
        user.toEntity()
    )
}

fun UserAuthResponse.toEntity(): UserEntity {
    return UserEntity(
        id,
        fullName,
        email,
        phone,
        location,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        meetingStarted,
        newGroups,
        inviteToMeeting,
        RegistrationType.REGULAR,
        unansweredQuestions?.size ?: 0,
        unreadNotificationsCount ?: 0,
        blockedUsersCount ?: 0,
        image?.toEntity(),
        displayName,
        description,
        bio,
        rating,
        rated ?: false,
        myRating,
        followed ?: false,
        followersCount ?: 0,
        allowRating ?: false,
        groupsCount.orZero(),
        role == MM_ROLE,
        community,
        visitedGroupsCount ?: 0,
        getDefaultCalendar().time,
        created_channels_count ?: 0,
        communities
    )
}

fun UserProfileResponse.toEntity(): UserEntity {
    return UserEntity(
        id,
        fullName,
        email,
        phone,
        location,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        meetingStarted,
        newGroups,
        inviteToMeeting,
        RegistrationType.fromRemoteString(socialType),
        unansweredQuestions?.size ?: 0,
        unreadNotificationsCount ?: 0,
        blockedUsersCount ?: 0,
        image?.toEntity(),
        null,
        description,
        null,
        rating,
        rated ?: false,
        myRating,
        followed ?: false,
        followersCount ?: 0,
        allowRating ?: false,
        groupsCount.orZero(),
        role == MM_ROLE,
        community,
        visitedGroupsCount,
        birthday?.toDate(),
        created_channels_count ?: 0,
        communities = communities
    )
}

fun UserLocal.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        fullName = fullName,
        email = email,
        phone = phone,
        location = location,
        createdAt = createdAt?.toDate(),
        updatedAt = updatedAt?.toDate(),
        meetingStarted = meetingStarted,
        newGroups = newGroups,
        inviteToMeeting = inviteToMeeting,
        registrationType = RegistrationType.fromRemoteString(registrationType),
        unansweredQuestionsCount = unansweredQuestionsCount,
        unreadNotificationsCount = unreadNotificationsCount,
        blockedUsersCount = blockedUsersCount,
        image = image?.toEntity(),
        displayName = displayName,
        description = description,
        bio = bio,
        rating = rating,
        isMasterMind = isMasterMind,
        community = community,
        visitedGroupCount = this@toUserEntity.visitedGroupCount,
        birthday = this@toUserEntity.birthday?.toShortDate(),
        communities = communities,
        rated = rated,
        myRating = myRating,
        followed = followed,
        allowRating = allowRating,
        groupsCount = groupsCount,
        created_channels_count = created_channels_count,
        followersCount = followersCount
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

fun SearchUsersResponse.toEntity(): AttendeeEntity {
    return AttendeeEntity(
        id,
        fullName,
        email,
        image?.url
    )
}