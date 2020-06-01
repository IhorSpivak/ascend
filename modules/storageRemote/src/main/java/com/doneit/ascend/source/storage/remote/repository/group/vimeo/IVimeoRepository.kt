package com.doneit.ascend.source.storage.remote.repository.group.vimeo

import com.doneit.ascend.source.storage.remote.data.response.ActivateLiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse

interface IVimeoRepository {
    suspend fun createLiveStream(title: String) : RemoteResponse<LiveEventResponse, String>

    suspend fun updateLiveStream(liveEventId: Long) : RemoteResponse<ActivateLiveEventResponse, String>
}