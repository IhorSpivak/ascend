package com.doneit.ascend.presentation.models.user

import com.doneit.ascend.domain.entity.user.RegistrationType
import com.doneit.ascend.domain.entity.user.UserEntity

fun UserEntity.toPresentation(): PresentationUserModel {
    return PresentationUserModel(
        id,
        fullName,
        email,
        phone,
        location,
        createdAt,
        updatedAt,
        meetingStarted,
        newGroups,
        inviteToMeeting,
        registrationType.toPresentation(),
        unansweredQuestionsCount,
        unreadNotificationsCount,
        image,
        displayName,
        description,
        bio,
        rating,
        isMasterMind,
        community,
        visitedGroupCount,
        birthday
    )
}

private fun RegistrationType.toPresentation(): PresentationRegistrationType {
    return when (this) {
        RegistrationType.FACEBOOK -> PresentationRegistrationType.FACEBOOK
        RegistrationType.GOOGLE -> PresentationRegistrationType.GOOGLE
        RegistrationType.TWITTER -> PresentationRegistrationType.TWITTER
        RegistrationType.REGULAR -> PresentationRegistrationType.REGULAR
    }
}