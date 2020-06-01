package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.ActivateLiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity

interface IVimeoGateway {
    suspend fun createLiveStream(title: String) : ResponseEntity<LiveEventEntity, String>

    suspend fun updateLiveStream(liveEventId: Long) : ResponseEntity<ActivateLiveEventEntity, String>
}