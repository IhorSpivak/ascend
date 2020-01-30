package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.GroupCredentialsModel
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.source.storage.remote.data.response.*
import com.doneit.ascend.source.storage.remote.data.response.group.GroupCredentialsResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.group.ParticipantResponse
import com.doneit.ascend.source.storage.remote.data.response.group.SocketEventMessage

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
    return GroupEntity(
        id,
        name,
        description,
        startTime?.toDate(),
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
        daysOfWeek?.map { it.toCalendarDay() }
    )
}

fun String.toGroupType(): GroupType? {
    return GroupType.valueOf(this.toUpperCase())
}

fun GroupCredentialsResponse.toEntity(): GroupCredentialsModel {
    return GroupCredentialsModel(
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
        isSpeaker
    )
}
