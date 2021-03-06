package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.user.RegistrationType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.gateway.common.mapper.Constants.MM_ROLE
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteStringShort
import com.doneit.ascend.source.storage.local.data.ImageLocal
import com.doneit.ascend.source.storage.local.data.ThumbnailLocal
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.ThumbnailResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserAuthResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserProfileResponse

fun UserEntity.toUserLocal(): UserLocal {
    return UserLocal(
        id = id,
        fullName = fullName,
        email = email,
        phone = phone,
        location = location,
        createdAt = createdAt?.toRemoteString(),
        updatedAt = updatedAt?.toRemoteString(),
        meetingStarted = meetingStarted,
        newGroups = newGroups,
        inviteToMeeting = inviteToMeeting,
        registrationType = registrationType.toString(),
        unansweredQuestionsCount = unansweredQuestionsCount,
        unreadNotificationsCount = unreadNotificationsCount,
        blockedUsersCount = blockedUsersCount,
        image = image?.toLocal(),
        displayName = displayName,
        description = description,
        bio = bio,
        rating = rating,
        isMasterMind = isMasterMind,
        visitedGroupCount = visitedGroupCount,
        community = community,
        birthday = birthday?.toRemoteStringShort(),
        communities = communities,
        rated = rated,
        allowRating = allowRating,
        followed = followed,
        groupsCount = groupsCount,
        myRating = myRating,
        created_channels_count = created_channels_count,
        followersCount = followersCount,
        haveSubscription = haveSubscription,
        subscriptionTrial = subscriptionTrial,
        subscriptionCanceled = subscriptionCanceled,
        stripeFieldsNeeded = stripeFieldsNeeded,
        stripeRequiredFieldsFilled = stripeRequiredFieldsFilled
    )
}

fun UserLocal.merge(newModel: UserProfileResponse): UserLocal {
    return UserLocal(
        id = newModel.id,
        fullName = newModel.fullName,
        email = newModel.email,
        phone = newModel.phone,
        location = newModel.location,
        createdAt = newModel.createdAt,
        updatedAt = newModel.updatedAt,
        meetingStarted = newModel.meetingStarted,
        newGroups = newModel.newGroups,
        inviteToMeeting = newModel.inviteToMeeting,
        registrationType = RegistrationType.fromRemoteString(newModel.socialType).toString(),
        unansweredQuestionsCount = unansweredQuestionsCount,
        unreadNotificationsCount = unreadNotificationsCount,
        blockedUsersCount = newModel.blockedUsersCount ?: 0,
        image = newModel.image?.toLocal(),
        displayName = displayName,
        description = description,
        bio = newModel.bio,
        rating = rating,
        isMasterMind = newModel.role == MM_ROLE,
        visitedGroupCount = visitedGroupCount,
        community = newModel.community,
        birthday = newModel.birthday,
        communities = communities,
        rated = rated,
        allowRating = allowRating,
        followed = followed,
        groupsCount = groupsCount,
        myRating = myRating,
        created_channels_count = newModel.created_channels_count ?: 0,
        followersCount = followersCount,
        haveSubscription = haveSubscription,
        subscriptionTrial = subscriptionTrial,
        subscriptionCanceled = subscriptionCanceled,
        stripeFieldsNeeded = stripeFieldsNeeded,
        stripeRequiredFieldsFilled = stripeRequiredFieldsFilled
    )
}

fun UserLocal.merge(newModel: UserAuthResponse): UserLocal {
    return UserLocal(
        id = newModel.id,
        fullName = newModel.fullName,
        email = newModel.email,
        phone = newModel.phone,
        location = newModel.location,
        createdAt = newModel.createdAt,
        updatedAt = newModel.updatedAt,
        meetingStarted = newModel.meetingStarted,
        newGroups = newModel.newGroups,
        inviteToMeeting = newModel.inviteToMeeting,
        registrationType = registrationType,
        unansweredQuestionsCount = unansweredQuestionsCount,
        unreadNotificationsCount = unreadNotificationsCount,
        blockedUsersCount = newModel.blockedUsersCount ?: 0,
        image = newModel.image?.toLocal(),
        displayName = newModel.displayName,
        description = newModel.description,
        bio = newModel.bio,
        rating = newModel.rating,
        isMasterMind = newModel.role == MM_ROLE,
        visitedGroupCount = visitedGroupCount,
        community = newModel.community,
        birthday = birthday,
        communities = communities,
        rated = rated,
        allowRating = allowRating,
        followed = followed,
        groupsCount = groupsCount,
        myRating = myRating,
        created_channels_count = newModel.created_channels_count ?: 0,
        followersCount = followersCount,
        haveSubscription = newModel.haveSubscription ?: false,
        subscriptionTrial = newModel.subscriptionTrial ?: false,
        subscriptionCanceled = newModel.subscriptionCanceled ?: false,
        stripeFieldsNeeded = newModel.stripeFieldsNeeded.orEmpty(),
        stripeRequiredFieldsFilled = newModel.stripeRequiredFieldsFilled ?: false
    )
}

fun ImageResponse.toLocal(): ImageLocal {
    return ImageLocal(
        -1,
        url,
        thumbnail?.toLocal()
    )
}

fun ThumbnailResponse.toLocal(): ThumbnailLocal {
    return ThumbnailLocal(
        -1,
        url
    )
}
