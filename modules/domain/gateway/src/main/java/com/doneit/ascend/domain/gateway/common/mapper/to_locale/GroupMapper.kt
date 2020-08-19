package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.Banner
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.NoteEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.local.data.dto.GroupFilter

fun GroupListDTO.toLocal(isUpcoming: Boolean = false): GroupFilter {
    return GroupFilter(
        sortType = sortType?.ordinal,
        groupStatus = groupStatus?.ordinal,
        groupType = groupType?.ordinal,
        userId = userId,
        isUpcoming = isUpcoming

    )
}

fun GroupEntity.toLocal(): GroupLocal {
    return GroupLocal(
        id = id,
        name = name,
        description = description,
        startTime = startTime?.toRemoteString(),
        status = status?.ordinal,
        groupType = groupType?.ordinal,
        price = price,
        image = image?.toLocal(),
        meetingsCount = meetingsCount,
        pastMeetingsCount = pastMeetingsCount,
        passedCount = passedCount,
        createdAt = createdAt?.toRemoteString(),
        updatedAt = updatedAt?.toRemoteString(),
        owner = owner?.toLocal(),
        banner = banner?.toLocal(),
        subscribed = subscribed,
        invited = invited,
        blocked = blocked,
        participantsCount = participantsCount,
        invitesCount = invitesCount,
        daysOfWeek = daysOfWeek.map { it.ordinal },
        note = note?.toLocale(),
        meetingFormat = meetingFormat,
        tag = tag?.toLocal(),
        isPrivate = isPrivate,
        dates = dates,
        themes = themes,
        duration = duration,
        community = community
    )
}

fun Banner.toLocal() = BannerLocal(
    title = title,
    bannerType = bannerType
)

fun OwnerEntity.toLocal(): OwnerLocal {
    return OwnerLocal(
        id = id,
        fullName = fullName,
        image = image?.toLocal(),
        rating = rating,
        followed = followed,
        location = location.orEmpty(),
        connected = connected
    )
}

fun TagEntity.toLocal(): TagLocal {
    return TagLocal(
        id = id,
        tag = tag
    )
}

fun NoteEntity.toLocale(): NoteLocal {
    return NoteLocal(
        id = -1,
        content = content,
        updatedAt = updatedAt.toRemoteString()
    )
}