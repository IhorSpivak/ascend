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

fun String.toGroupType(): GroupType? {
    return GroupType.valueOf(this.toUpperCase())
}

fun GroupResponse.toEntity(): GroupEntity {
    return GroupEntity(
        id,
        name,
        description,
        startTime,
        groupType?.toGroupType(),
        price,
        image?.toEntity(),
        createdAt,
        updatedAt,
        owner?.toEntity(),
        participantsCount
    )
}

fun GroupDetailsResponse.toEntity(): GroupDetailsEntity {
    return GroupDetailsEntity(
        id,
        name,
        description,
        startTime,
        groupType?.toGroupType(),
        price,
        image?.toEntity(),
        meetingsCount,
        createdAt,
        updatedAt,
        owner?.toEntity(),
        subscribed,
        invited,
        participantsCount,
        invitesCount
    )
}