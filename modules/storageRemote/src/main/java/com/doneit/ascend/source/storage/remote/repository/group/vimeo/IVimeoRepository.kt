package com.doneit.ascend.source.storage.remote.repository.group.vimeo

import com.doneit.ascend.source.storage.remote.data.response.ActivateLiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.M3u8Response
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse

interface IVimeoRepository {
    suspend fun createLiveStream(title: String) : RemoteResponse<LiveEventResponse, String>

    suspend fun activateLiveStream(liveEventId: Long) : RemoteResponse<ActivateLiveEventResponse, String>

    suspend fun getM3u8PlaybackUrl(liveEventId: Long) : RemoteResponse<M3u8Response, String>
}