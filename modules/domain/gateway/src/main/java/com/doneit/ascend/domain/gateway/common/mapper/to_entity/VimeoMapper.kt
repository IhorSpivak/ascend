package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.vimeo.ActivateLiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.M3u8Entity
import com.doneit.ascend.source.storage.remote.data.response.ActivateLiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.M3u8Response

fun LiveEventResponse.toEntity(): LiveEventEntity {
    return LiveEventEntity(
        link,
        rtmpLink
    )
}

fun ActivateLiveEventResponse.toEntity(): ActivateLiveEventEntity {
    return ActivateLiveEventEntity(
        this.live.streamKey
    )
}
fun M3u8Response.toEntity(): M3u8Entity {
    return M3u8Entity()
}