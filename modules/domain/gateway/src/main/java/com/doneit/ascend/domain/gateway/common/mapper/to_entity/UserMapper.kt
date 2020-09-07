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
        token = token,
        userEntity = user.toEntity()
    )
}

fun UserAuthResponse.toEntity(): UserEntity {
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
        registrationType = RegistrationType.REGULAR,
        unansweredQuestionsCount = unansweredQuestions?.size ?: 0,
        unreadNotificationsCount = unreadNotificationsCount ?: 0,
        blockedUsersCount = blockedUsersCount ?: 0,
        image = image?.toEntity(),
        displayName = displayName,
        description = description,
        bio = bio,
        rating = rating,
        rated = rated ?: false,
        myRating = myRating,
        followed = followed ?: false,
        followersCount = followersCount ?: 0,
        allowRating = allowRating ?: false,
        groupsCount = groupsCount.orZero(),
        isMasterMind = role == MM_ROLE,
        community = community,
        visitedGroupCount = visitedGroupsCount ?: 0,
        birthday = getDefaultCalendar().time,
        created_channels_count = created_channels_count ?: 0,
        communities = communities,
        haveSubscription = haveSubscription ?: false,
        subscriptionTrial = subscriptionTrial ?: false,
        subscriptionCanceled = subscriptionCanceled ?: false,
        stripeFieldsNeeded = stripeFieldsNeeded.orEmpty(),
        stripeRequiredFieldsFilled = stripeRequiredFieldsFilled ?: false
    )
}

fun UserProfileResponse.toEntity(): UserEntity {
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
        registrationType = RegistrationType.fromRemoteString(socialType),
        unansweredQuestionsCount = unansweredQuestions?.size ?: 0,
        unreadNotificationsCount = unreadNotificationsCount ?: 0,
        blockedUsersCount = blockedUsersCount ?: 0,
        image = image?.toEntity(),
        displayName = null,
        description = description,
        bio = bio,
        rating = rating,
        rated = rated ?: false,
        myRating = myRating,
        followed = followed ?: false,
        followersCount = followersCount ?: 0,
        allowRating = allowRating ?: false,
        groupsCount = groupsCount.orZero(),
        isMasterMind = role == MM_ROLE,
        community = community,
        visitedGroupCount = visitedGroupsCount,
        birthday = birthday?.toDate(),
        created_channels_count = created_channels_count ?: 0,
        communities = communities,
        haveSubscription = haveSubscription ?: false,
        subscriptionTrial = subscriptionTrial ?: false,
        subscriptionCanceled = subscriptionCanceled ?: false,
        stripeFieldsNeeded = stripeFieldsNeeded.orEmpty(),
        stripeRequiredFieldsFilled = stripeRequiredFieldsFilled ?: false
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
        followersCount = followersCount,
        haveSubscription = haveSubscription,
        subscriptionTrial = subscriptionTrial,
        subscriptionCanceled = subscriptionCanceled,
        stripeFieldsNeeded = stripeFieldsNeeded,
        stripeRequiredFieldsFilled = stripeRequiredFieldsFilled
    )
}

fun RateResponse.toEntity(): RateEntity {
    return RateEntity(
        value = value,
        userId = userId,
        fullName = fullName,
        image = image.toEntity(),
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate()
    )
}

fun SearchUsersResponse.toEntity(): AttendeeEntity {
    return AttendeeEntity(
        id = id,
        fullName = fullName,
        email = email,
        imageUrl = image?.url
    )
}