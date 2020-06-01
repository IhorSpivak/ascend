package com.doneit.ascend.domain.use_case.interactor.vimeo

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.ActivateLiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity
import com.doneit.ascend.domain.entity.vimeo.M3u8Entity

interface VimeoUseCase {
    suspend fun createLiveStream(title: String) : ResponseEntity<LiveEventEntity, String>

    suspend fun updateLiveStream(liveEventId: Long) : ResponseEntity<ActivateLiveEventEntity, String>

    suspend fun getM3u8(liveEventId: Long) : ResponseEntity<M3u8Entity, String>
}