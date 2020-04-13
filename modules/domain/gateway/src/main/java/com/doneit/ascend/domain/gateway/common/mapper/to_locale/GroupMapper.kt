package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.NoteEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.local.data.dto.GroupFilter

fun GroupListDTO.toLocal(): GroupFilter {
    return GroupFilter(
        sortType?.ordinal,
        groupStatus?.ordinal
    )
}

fun GroupEntity.toLocal(): GroupLocal {
    return GroupLocal(
        id,
        name,
        description,
        startTime?.toRemoteString(),
        status?.ordinal,
        groupType?.ordinal,
        price,
        image?.toLocal(),
        meetingsCount,
        pastMeetingsCount,
        passedCount,
        createdAt?.toRemoteString(),
        updatedAt?.toRemoteString(),
        owner?.toLocal(),
        subscribed,
        invited,
        blocked,
        participantsCount,
        invitesCount,
        daysOfWeek?.map { it.ordinal },
        note?.toLocale(),
        meetingFormat,
        tag?.toLocal(),
        isPrivate,
        dates,
        themes
    )
}

fun OwnerEntity.toLocal(): OwnerLocal {
    return OwnerLocal(
        id,
        fullName,
        image?.toLocal(),
        rating,
        followed,
        location?:""
    )
}

fun TagEntity.toLocal(): TagLocal {
    return TagLocal(
        id,
        tag
    )
}

fun NoteEntity.toLocale(): NoteLocal {
    return NoteLocal(
        -1,
        content,
        updatedAt.toRemoteString()
    )
}