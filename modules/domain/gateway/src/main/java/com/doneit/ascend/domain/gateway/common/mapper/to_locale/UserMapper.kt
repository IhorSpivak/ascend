package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteStringShort
import com.doneit.ascend.source.storage.local.data.UserLocal

fun UserEntity.toUserLocal(): UserLocal {
    return UserLocal(
        id = id,
        name = this@toUserLocal.fullName,
        email = this@toUserLocal.email,
        phone = this@toUserLocal.phone,
        createdAt = this@toUserLocal.createdAt?.toRemoteString(),
        updatedAt = this@toUserLocal.updatedAt?.toRemoteString(),
        isMasterMind = this@toUserLocal.isMasterMind,
        rating = this@toUserLocal.rating,
        community = this@toUserLocal.community,
        description = this@toUserLocal.description,
        displayName = this@toUserLocal.displayName,
        inviteToMeeting = this@toUserLocal.inviteToMeeting,
        newGroups = this@toUserLocal.newGroups,
        meetingStarted = this@toUserLocal.meetingStarted,
        location = this@toUserLocal.location,
        role = this@toUserLocal.role,
        bio = this@toUserLocal.bio,
        imageURL = image?.url,
        thumbURL = image?.thumbnail?.url,
        birthday = birthday?.toRemoteStringShort(),
        visitedGroupCount = visitedGroupCount
    )
}