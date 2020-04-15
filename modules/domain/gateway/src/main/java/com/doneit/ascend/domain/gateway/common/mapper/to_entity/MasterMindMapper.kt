package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.source.storage.local.data.ImageLocal
import com.doneit.ascend.source.storage.local.data.MasterMindLocal
import com.doneit.ascend.source.storage.local.data.ThumbnailLocal
import com.doneit.ascend.source.storage.remote.data.response.MasterMindResponse

fun MasterMindResponse.toEntity(): MasterMindEntity {
    return MasterMindEntity(
        id,
        fullName,
        displayName,
        description,
        location,
        bio,
        groupsCount,
        rating,
        followed,
        rated,
        image?.toEntity(),
        allowRating,
        myRating
    )
}

fun MasterMindLocal.toEntity(): MasterMindEntity {
    return MasterMindEntity(
        id,
        fullName,
        displayName,
        description,
        location,
        bio,
        groupsCount,
        rating,
        followed,
        rated,
        image?.toEntity(),
        allowRating,
        myRating
    )
}

fun ImageLocal.toEntity(): ImageEntity {
    return ImageEntity(
        url,
        thumbnail?.toEntity()
    )
}

fun ThumbnailLocal.toEntity(): ThumbnailEntity {
    return ThumbnailEntity(
        url
    )
}