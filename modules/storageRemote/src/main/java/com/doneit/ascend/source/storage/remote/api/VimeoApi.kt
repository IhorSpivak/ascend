package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.LiveEventResponse
import com.doneit.ascend.source.storage.remote.data.response.M3u8Response
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VimeoApi {
    @POST("/me/live_events")
    fun createLiveEvent(@Query("title") title: String) : Deferred<Response<LiveEventResponse>>

    @POST("me/live_events/{live_event_id}/activate")
    fun activateLiveEvent(@Path("live_event_id") liveEventId: Long) : Deferred<Response<LiveEventResponse>>

    @POST("me/live_events/{live_event_id}/m3u8_playback")
    fun getM3u8(@Path("live_event_id") liveEventId: Long) : Deferred<Response<M3u8Response>>
}