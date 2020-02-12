package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.dto.GroupListDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.NoteEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.NoteLocal
import com.doneit.ascend.source.storage.local.data.OwnerLocal
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
        note?.toLocale()
    )
}

fun OwnerEntity.toLocal(): OwnerLocal {
    return OwnerLocal(
        id,
        fullName,
        image.toLocal(),
        rating,
        followed
    )
}

fun NoteEntity.toLocale(): NoteLocal {
    return NoteLocal(
        -1,
        content,
        updatedAt.toRemoteString()
    )
}