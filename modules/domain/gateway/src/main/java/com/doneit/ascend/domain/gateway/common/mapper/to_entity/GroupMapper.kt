package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.group.NoteEntity
import com.doneit.ascend.domain.gateway.common.applyDaysOffset
import com.doneit.ascend.domain.gateway.common.getDayOffset
import com.doneit.ascend.source.storage.local.data.GroupLocal
import com.doneit.ascend.source.storage.local.data.NoteLocal
import com.doneit.ascend.source.storage.local.data.OwnerLocal
import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.ThumbnailResponse
import com.doneit.ascend.source.storage.remote.data.response.group.*

fun ThumbnailResponse.toEntity(): ThumbnailEntity {
    return ThumbnailEntity(
        url
    )
}

fun ImageResponse.toEntity(): ImageEntity {
    return ImageEntity(
        url,
        thumbnail?.toEntity()
    )
}

fun OwnerResponse.toEntity(): OwnerEntity {
    return OwnerEntity(
        id,
        fullName,
        image.toEntity(),
        rating,
        followed
    )
}

fun GroupResponse.toEntity(): GroupEntity {
    val startT = startTime.toDate()!!
    val dayOffset = -1 * startT.getDayOffset()

    return GroupEntity(
        id,
        name,
        description,
        startT,
        status?.toGroupStatus(),
        groupType?.toGroupType(),
        price / 100,
        image?.toEntity(),
        meetingsCount,
        0,//todo map from server
        createdAt?.toDate(),
        updatedAt?.toDate(),
        owner?.toEntity(),
        subscribed,
        invited,
        blocked,
        participantsCount,
        invitesCount,
        daysOfWeek.applyDaysOffset(dayOffset).map { it.toCalendarDay() },
        note?.toEntity()
    )
}

fun NoteResponse.toEntity(): NoteEntity {
    return NoteEntity(
        content,
        updatedAt.toDate()!!
    )
}

fun String.toGroupStatus(): GroupStatus? {
    return GroupStatus.valueOf(this.toUpperCase())
}

fun String.toGroupType(): GroupType? {
    return GroupType.valueOf(this.toUpperCase())
}

fun GroupCredentialsResponse.toEntity(): GroupCredentialsDTO {
    return GroupCredentialsDTO(
        name,
        token
    )
}

fun SocketEventMessage.toEntity(): SocketEventEntity {
    val event = SocketEvent.fromRemoteString(event!!)
    return SocketEventEntity(
        event,
        SocketUserEntity(
            userId,
            fullName,
            image?.toEntity(),
            event == SocketEvent.RISE_A_HAND
        )
    )
}

private fun Int.toCalendarDay(): CalendarDayEntity {
    return CalendarDayEntity.values()[this]
}

fun ParticipantResponse.toEntity(): ParticipantEntity {
    return ParticipantEntity(
        id,
        fullName,
        image?.toEntity(),
        isHandRisen,
        isConnected,
        isVisited,
        isBlocked,
        isSpeaker,
        isMuted
    )
}

fun GroupLocal.toEntity(): GroupEntity {
    return GroupEntity(
        id,
        name,
        description,
        startTime?.toDate(),
        status?.toGroupStatus(),
        groupType?.toGroupType(),
        price,
        image?.toEntity(),
        meetingsCount,
        passedCount,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        owner?.toEntity(),
        subscribed,
        invited,
        blocked,
        participantsCount,
        invitesCount,
        daysOfWeek?.map { CalendarDayEntity.values()[it] },
        note?.toLocale()
    )
}

fun NoteLocal.toLocale(): NoteEntity {
    return NoteEntity(
        content,
        updatedAt.toDate()!!
    )
}

fun Int.toGroupStatus(): GroupStatus {
    return GroupStatus.values()[this]
}

fun Int.toGroupType(): GroupType {
    return GroupType.values()[this]
}

fun OwnerLocal.toEntity(): OwnerEntity {
    return OwnerEntity(
        id,
        fullName,
        image.toEntity(),
        rating,
        followed
    )
}