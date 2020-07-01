package com.doneit.ascend.domain.use_case.interactor.vimeo

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.ActivateLiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.M3u8Entity
import com.doneit.ascend.domain.use_case.gateway.IVimeoGateway

class VimeoInteractor(
    private val vimeoGateway: IVimeoGateway
) : VimeoUseCase{
    override suspend fun createLiveStream(title: String): ResponseEntity<LiveEventEntity, String> {
        return vimeoGateway.createLiveStream(title)
    }

    override suspend fun activateLiveStream(liveEventId: Long): ResponseEntity<ActivateLiveEventEntity, String> {
        return vimeoGateway.activateLiveStream(liveEventId)
    }

    override suspend fun getM3u8(liveEventId: Long): ResponseEntity<M3u8Entity, String> {
        return vimeoGateway.getM3u8(liveEventId)
    }

}