package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.MasterMindEntity
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
        image.toEntity(),
        allowRating
    )
}