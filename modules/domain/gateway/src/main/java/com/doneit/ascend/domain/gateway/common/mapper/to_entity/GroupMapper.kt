package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.source.storage.remote.data.response.*

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
        price,
        image?.toEntity(),
        meetingsCount,
        0,//todo map from server
        createdAt?.toDate(),
        updatedAt?.toDate(),
        owner?.toEntity(),
        subscribed,
        invited,
        participantsCount,
        invitesCount,
        daysOfWeek?.map { it.toCalendarDay() }
    )
}

fun String.toGroupType(): GroupType? {
    return GroupType.valueOf(this.toUpperCase())
}

private fun Int.toCalendarDay(): CalendarDayEntity {
    return CalendarDayEntity.values()[this]
}
