package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse

fun LiveEventResponse.toEntity(): LiveEventEntity {
    return LiveEventEntity(
        link,
        streamKey,
        rtmpLink
    )
}