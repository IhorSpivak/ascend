package com.doneit.ascend.domain.use_case.interactor.vimeo

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.vimeo.LiveEventEntity

interface VimeoUseCase {
    suspend fun createLiveStream(title: String) : ResponseEntity<LiveEventEntity, String>

    suspend fun updateLiveStream(liveEventId: Long) : ResponseEntity<LiveEventEntity, String>
}