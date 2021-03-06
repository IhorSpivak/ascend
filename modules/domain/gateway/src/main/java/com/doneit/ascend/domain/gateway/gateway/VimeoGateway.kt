package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.ActivateLiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.M3u8Entity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.use_case.gateway.IVimeoGateway
import com.doneit.ascend.source.storage.remote.repository.group.vimeo.IVimeoRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class VimeoGateway(
    errors: NetworkManager,
    private val remote: IVimeoRepository
) : IVimeoGateway {
    override suspend fun createLiveStream(title: String): ResponseEntity<LiveEventEntity, String> {
        return remote.createLiveStream(title).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it
            }
        )
    }

    override suspend fun activateLiveStream(liveEventId: Long): ResponseEntity<ActivateLiveEventEntity, String> {
        return remote.activateLiveStream(liveEventId).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it
            }
        )
    }

    override suspend fun getM3u8(liveEventId: Long): ResponseEntity<M3u8Entity, String> {
        return remote.getM3u8PlaybackUrl(liveEventId).toResponseEntity(
            {
                it?.toEntity()
            },
            {
                it
            }
        )
    }

}
