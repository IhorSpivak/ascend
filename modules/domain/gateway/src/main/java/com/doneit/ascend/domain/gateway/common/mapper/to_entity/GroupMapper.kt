package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.group.NoteEntity
import com.doneit.ascend.domain.gateway.common.applyDaysOffset
import com.doneit.ascend.domain.gateway.common.getDayOffset
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.TagResponse
import com.doneit.ascend.source.storage.remote.data.response.ThumbnailResponse
import com.doneit.ascend.source.storage.remote.data.response.group.*
import java.util.*

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
        image?.toEntity(),
        rating,
        followed,
        location
    )
}

fun TagResponse.toEntity(): TagEntity{
    return TagEntity(
        id,
        tag
    )
}

fun GroupResponse.toEntity(): GroupEntity {
    val startT = startTime.toDate()?: getDefaultCalendar().time
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
        getDays(daysOfWeek, dayOffset),
        note?.toEntity(),
        meetingFormat,
        tag?.toEntity(),
        invites?.map { it.toEntity() },
        private,
        pastMeetingsCount,
        dates,
        themes
    )
}
private fun getDays(list: List<Int>?, dayOffset: Int): List<CalendarDayEntity>{
    return list?.applyDaysOffset(dayOffset)?.map { it.toCalendarDay() } ?: listOf(CalendarDayEntity.SUNDAY)
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
        note?.toLocale(),
        meetingFormat,
        tag?.toEntity(), emptyList(),
        isPrivate,
        pastMeetingsCount,
        dates,
        themes
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
        image?.toEntity(),
        rating,
        followed,
        location
    )
}

fun TagLocal.toEntity(): TagEntity{
    return TagEntity(
        id,
        tag
    )
}
