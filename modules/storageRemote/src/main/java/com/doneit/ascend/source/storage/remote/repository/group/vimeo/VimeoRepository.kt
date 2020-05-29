package com.doneit.ascend.source.storage.remote.repository.group.vimeo

import com.doneit.ascend.source.storage.remote.api.VimeoApi
import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class VimeoRepository(
    gson: Gson,
    private val api: VimeoApi
) : BaseRepository(gson), IVimeoRepository {
    override suspend fun createLiveStream(title: String): RemoteResponse<LiveEventResponse, String> {
        return execute( {
            api.createLiveEvent(title)
        }, String::class.java)
    }

    override suspend fun updateLiveStream(liveEventId: Long): RemoteResponse<LiveEventResponse, String> {
        return execute( {
            api.activateLiveEvent(liveEventId)
        }, String::class.java)
    }

}