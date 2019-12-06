package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.OwnerEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.doneit.ascend.source.storage.remote.data.response.OwnerResponse
import com.doneit.ascend.source.storage.remote.data.response.ThumbnailResponse

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
        startTime,
        groupType,
        price,
        image.toEntity(),
        createdAt,
        updatedAt,
        owner.toEntity(),
        participantsCount
    )
}