package com.doneit.ascend.domain.use_case.interactor.vimeo

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.domain.use_case.gateway.IVimeoGateway

class VimeoInteractor(
    private val vimeoGateway: IVimeoGateway
) : VimeoUseCase{
    override suspend fun createLiveStream(title: String): ResponseEntity<LiveEventEntity, String> {
        return vimeoGateway.createLiveStream(title)
    }

    override suspend fun updateLiveStream(liveEventId: Long): ResponseEntity<LiveEventEntity, String> {
        return vimeoGateway.updateLiveStream(liveEventId)
    }

}